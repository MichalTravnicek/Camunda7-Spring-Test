package com.example.security.jwt.controller;

import java.util.Objects;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.security.jwt.config.JwtTokenUtil;
import com.example.security.jwt.model.JwtRequest;
import com.example.security.jwt.model.JwtResponse;

@Path("/")
public class JwtAuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationController.class);

    public JwtAuthenticationController() {
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Path("/authenticate")
    @POST
    @Produces({"application/json"})
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.error("User is disabled:" + username);
            throw new NotAuthorizedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials:" + username);
            throw new NotAuthorizedException("INVALID_CREDENTIALS", e);
        }
    }
}
