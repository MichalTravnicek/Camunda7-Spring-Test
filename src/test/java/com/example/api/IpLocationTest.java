package com.example.api;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;

import com.example.api.iplocation.IpLocationApiClient;

@RestClientTest(components = {IpLocationApiClient.class, RestTemplateConfig.class})
public class IpLocationTest {

    @Autowired
    IpLocationApiClient client;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void testApiConnection() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("city","Brno");
        server.expect(anything()).andRespond(withSuccess(jsonObject.toString(), MediaType.APPLICATION_JSON));
        String ipAddress = "12.201.250.155";
        final String location = client.getLocation(ipAddress);
        Assertions.assertThat(location).isEqualTo("Brno");
    }

    @Test
    public void testApiError() throws JSONException {
        final JSONObject errorObject = new JSONObject();
        errorObject.put("code","123");
        errorObject.put("type","invalid request");
        errorObject.put("info","Some description");
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("success","false");
        jsonObject.put("error", errorObject);
        server.expect(anything()).andRespond(withSuccess(jsonObject.toString(), MediaType.APPLICATION_JSON));
        String ipAddress = "12.201.250.155";
        org.junit.jupiter.api.Assertions.assertThrows(ApiError.class,() -> client.getLocation(ipAddress));
    }

    @Test
    public void testBlankResponseError() {
        server.expect(anything()).andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        String ipAddress = "12.201.250.155";
        org.junit.jupiter.api.Assertions.assertThrows(ApiError.class,() -> client.getLocation(ipAddress));
    }

    @Test
    public void testUnparseableResponseError() {
        server.expect(anything()).andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
        String ipAddress = "12.201.250.155";
        org.junit.jupiter.api.Assertions.assertThrows(ApiError.class,() -> client.getLocation(ipAddress));
    }

    @Test
    public void testExceptionResponseError() {
        server.expect(anything()).andRespond(withException(new IOException()));
        String ipAddress = "12.201.250.155";
        org.junit.jupiter.api.Assertions.assertThrows(ApiError.class,() -> client.getLocation(ipAddress));
    }

}
