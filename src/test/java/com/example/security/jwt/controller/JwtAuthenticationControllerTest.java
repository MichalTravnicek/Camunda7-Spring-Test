package com.example.security.jwt.controller;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtAuthenticationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRootPath(){
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/",
                String.class);
        Assertions.assertThat(response).isNotNull();
        //root path should not require authentication (Apache client does not follow redirect and gives 302)
        Assertions.assertThat(response.getStatusCodeValue()).isIn(200,302);
    }

    @Test
    public void testRestEnginePath(){
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/engine-rest/engine",
                String.class);
        Assertions.assertThat(response).isNotNull();
        //rest path requires authentication
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testRestEnginePathAuthenticated() throws JSONException {
        final String token = getToken(getJwtTokenResponse("user","password"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer "+ token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/engine-rest/engine",
                HttpMethod.GET, entity, String.class);
        Assertions.assertThat(response).isNotNull();
        //rest path requires authentication - when authorized returns 200
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testGetToken() throws JSONException {
        final String jwtToken = getToken(getJwtTokenResponse("user","password"));
        Assertions.assertThat(jwtToken).isNotEmpty();
    }

    @Test
    public void testGetTokenFail() throws JSONException {
        final ResponseEntity<String> tokenResponse = getJwtTokenResponse("bad_value","password");
        Assertions.assertThat(tokenResponse).isNotNull();
        Assertions.assertThat(tokenResponse.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testGetTokenFailBadPassword() throws JSONException {
        final ResponseEntity<String> tokenResponse = getJwtTokenResponse("user","bad_value");
        Assertions.assertThat(tokenResponse).isNotNull();
        Assertions.assertThat(tokenResponse.getStatusCodeValue()).isEqualTo(401);
    }

    private ResponseEntity<String> getJwtTokenResponse(String username, String password) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
        final ResponseEntity<String> authentication = restTemplate.postForEntity("http://localhost:" + port + "/engine-rest/authenticate", entity, String.class);
        Assertions.assertThat(authentication).isNotNull();
        return authentication;
    }

    private String getToken(ResponseEntity<String> response) throws JSONException {
        Assertions.assertThat(response.getBody()).contains("\"token\"");
        return new JSONObject(response.getBody()).getString("token");
    }


}