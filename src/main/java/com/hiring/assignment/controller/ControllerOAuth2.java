package com.hiring.assignment.controller;

import com.google.common.collect.ImmutableMap;
import com.hiring.assignment.dto.DtoResponse;
import com.hiring.assignment.oauth2.service.UserEntity;
import com.hiring.assignment.service.ServiceOAuth2;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/public")
public class ControllerOAuth2 {
    @Autowired
    ServiceOAuth2 serviceOAuth2;

    @PostMapping(value = "/oauth/token")
    public DtoResponse getJwtToken(@RequestBody UserEntity userEntity) throws URISyntaxException, ParseException {
        DtoResponse response = new DtoResponse();
        String accessString = serviceOAuth2.getJwtToken(userEntity);
        if (accessString != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Token Get Success");
            response.setBody(ImmutableMap.of("access_token", accessString));
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.name());
            response.setMessage("Token Get Failed");
        }
        return response;
    }

    @GetMapping(value = "/oauth/revokAllTokens")
    public DtoResponse revokAllTokens() throws URISyntaxException, ParseException {
        DtoResponse response = new DtoResponse();
        Boolean isRevoked = serviceOAuth2.revokAllTokens();
        if (isRevoked != null && isRevoked.booleanValue()) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Tokens Revoked Success");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.name());
            response.setMessage("Tokens Revoking Failed");
        }
        return response;
    }

}
