package com.after.life.chatai.dto;

import lombok.Data;

import java.time.Instant;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 17:39
 */
@Data
public class OAuth2BaseTokenDTO {
    private Instant expiresAt;
    private Instant issuedAt;
    private String tokenValue;

}
