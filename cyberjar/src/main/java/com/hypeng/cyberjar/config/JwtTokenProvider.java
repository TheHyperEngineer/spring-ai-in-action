package com.hypeng.cyberjar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${my.openai.api.key}")
    private String jwtToken;

    /**
     * Called on EVERY LLM request → always fresh JWT.
     * Put your enterprise auth logic here (call to auth service, JJWT signing, cached refresh, etc.).
     */
    public String getCurrentJwtToken() {
        return jwtToken; // placeholder
    }
}