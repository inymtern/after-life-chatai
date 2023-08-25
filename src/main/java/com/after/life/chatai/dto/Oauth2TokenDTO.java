package com.after.life.chatai.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 17:42
 */
@Data
public class Oauth2TokenDTO {

    private OAuth2AccessToken access_token;
    private OAuth2BaseTokenDTO refresh_token;

    @Data
    public static class OAuth2AccessToken {
        private Instant expiresAt;
        private Instant issuedAt;
        private String tokenValue;
        private List<String> scopes;
        private TokenType tokenType;
        @Data
        public static class TokenType {
            private String value;
        }
    }
}
