package com.hypeng.cyberjar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${my.openai.api.key}")
    private String jwtToken;

    public String getCurrentJwtToken() {
        return jwtToken;
    }
}