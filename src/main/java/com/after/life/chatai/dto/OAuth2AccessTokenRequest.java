package com.after.life.chatai.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 20:57
 */
@Data
@Builder
public class OAuth2AccessTokenRequest {

    private String grant_type ;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;


}
