package com.after.life.chatai.dto;

import lombok.Data;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 20:11
 */
@Data
public class OAuth2CodeRequest {
    private String response_type = "code";
    private String client_id;
    private String scope;
    private String state;
    private String redirect_uri;
}
