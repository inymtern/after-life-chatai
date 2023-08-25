package com.after.life.chatai.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.after.life.chatai.dto.OAuth2BaseTokenDTO;
import com.after.life.chatai.dto.Oauth2TokenDTO;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 17:18
 */
@Component
public class OAuth2AuthorizationService {

    private static final String AuthorizationCodeUrl = "/oauth2/authorize";
    private static final String AccessTokenUrl = "/oauth2/access-token";
    private static final String RefreshTokenUrl = "/oauth2/refresh-token";

    @Value("${config.oauth2.client_id}")
    private String clientId;
    @Value("${config.oauth2.client_secret}")
    private String clientSecret;
    @Value("${config.oauth2.server_address}")
    private String serverAddress;
    @Value("${config.oauth2.redirect_uri}")
    private String redirectUri;

    public String authorizationCodeParams(String state) {
        return buildAuthorizationCodeUrlParams(state);
    }


    public Oauth2TokenDTO fetchAccessTokenAndRefreshToken(String code) {
        try (HttpResponse httpResponse = HttpUtil.createPost(serverAddress + buildAccessTokenUrlParams(code))
                .execute()) {
            if (httpResponse.isOk()) {
                String body = httpResponse.body();
                return JSON.parseObject(body, Oauth2TokenDTO.class);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Oauth2TokenDTO refreshToken(String refreshToken) {
        try (HttpResponse httpResponse = HttpUtil.createPost(serverAddress + buildRefreshTokenUrlParams(refreshToken))
                .execute()) {
            if (httpResponse.isOk()) {
                String body = httpResponse.body();
                return JSON.parseObject(body, Oauth2TokenDTO.class);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private String buildAuthorizationCodeUrlParams( String state) {
        return StrUtil.format("?response_type={}&scope={}&client_id={}&redirect_uri={}&state={}",
                 "code", "user.info", clientId,redirectUri,state);
    }
    private String buildRefreshTokenUrlParams(String refreshToken) {
        return StrUtil.format("{}?grant_type={}&client_id={}&client_secret={}&refresh_token={}",
                RefreshTokenUrl, "refresh_token",  clientId,clientSecret,refreshToken);
    }
    private String buildAccessTokenUrlParams(  String code) {
        return StrUtil.format("{}?grant_type={}&code={}&client_id={}&client_secret={}&redirect_uri={}",
                AccessTokenUrl, "authorize_code", code, clientId,clientSecret,redirectUri);
    }
}
