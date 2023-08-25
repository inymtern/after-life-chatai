package com.after.life.chatai.controller;

import cn.hutool.core.map.MapBuilder;
import com.after.life.chatai.dto.*;
import com.after.life.chatai.remote.BlogOAuthFeignClient;
import com.after.life.chatai.service.OAuth2AuthorizationService;
import com.after.life.chatai.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 17:48
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2AuthorizationController {

    @Value("${config.oauth2.client_id}")
    private String clientId;
    @Value("${config.oauth2.client_secret}")
    private String clientSecret;
    @Value("${config.oauth2.redirect_uri}")
    private String redirectUri;

//    private static final String oauth_access_token_key = "chatai:oauth:access-token:";
    private static final String oauth_refresh_token_key = "chatai:oauth:refresh-token:";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final BlogOAuthFeignClient oAuthFeignClient;

    public OAuth2AuthorizationController(OAuth2AuthorizationService oAuth2AuthorizationService, BlogOAuthFeignClient oAuthFeignClient) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.oAuthFeignClient = oAuthFeignClient;
    }

    @GetMapping("/params")
    public ResponseEntity<?> authorizationCodeParams(String state) {
        String authorizationCodeParams = oAuth2AuthorizationService.authorizationCodeParams(state);
        return ResponseEntity.ok(authorizationCodeParams);
    }

    @GetMapping("/access-token")
    public ResponseEntity<?> fetchAccessToken(String code) {
        OAuth2AccessTokenRequest request = OAuth2AccessTokenRequest.builder()
                .grant_type("authorization_code")
                .code(code)
                .client_id(clientId)
                .client_secret(clientSecret)
                .redirect_uri(redirectUri)
                .build();
        ResponseEntity<Oauth2TokenDTO> oauth2TokenDTOResponseEntity = oAuthFeignClient.fetchAccessToken(request);
        if (oauth2TokenDTOResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Oauth2TokenDTO body = oauth2TokenDTOResponseEntity.getBody();
            assert body != null;
            Oauth2TokenDTO.OAuth2AccessToken accessToken = body.getAccess_token();
            OAuth2BaseTokenDTO refreshToken = body.getRefresh_token();

            ResponseEntity<OAuth2UserInfoDTO> userinfoResp = oAuthFeignClient.userinfo(accessToken.getTokenType().getValue() + " " +accessToken.getTokenValue());
            if (userinfoResp.getStatusCode().equals(HttpStatus.OK)) {
                OAuth2UserInfoDTO userInfoDTO = userinfoResp.getBody();
                assert userInfoDTO != null;
                RedisUtil.set(oauth_refresh_token_key + accessToken.getTokenValue(), refreshToken.getTokenValue(), getSec(refreshToken.getExpiresAt()));
//                RedisUtil.set(oauth_refresh_token_key + userInfoDTO.getId(), refreshToken.getTokenValue(), getSec(refreshToken.getExpiresAt()));
                return ResponseEntity.ok(MapBuilder.create()
                        .put("token", accessToken.getTokenValue())
                        .put("userinfo", userInfoDTO)
                        .build());
            }else {
                return ResponseEntity.status(500).body("feign client exec userinfo fail");
            }
        } else {
            return ResponseEntity.status(500).body("feign client exec access-token fail");
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> fetchRefreshToken(@RequestHeader("Authorization") String token) {
        token = token.substring("bearer ".length());
        Object o = RedisUtil.get(oauth_refresh_token_key + token);
        if(o == null) return ResponseEntity.status(500).body("refresh token is expired");
        OAuth2RefreshTokenRequest request = OAuth2RefreshTokenRequest.builder()
                .grant_type("refresh_token")
                .client_id(clientId)
                .client_secret(clientSecret)
                .refresh_token(o.toString())
                .build();
        ResponseEntity<Oauth2TokenDTO> oauth2TokenDTOResponseEntity = oAuthFeignClient.fetchRefreshToken(request);
        if (oauth2TokenDTOResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Oauth2TokenDTO body = oauth2TokenDTOResponseEntity.getBody();
            assert body != null;
            Oauth2TokenDTO.OAuth2AccessToken accessToken = body.getAccess_token();
            OAuth2BaseTokenDTO refreshToken = body.getRefresh_token();

            RedisUtil.set(oauth_refresh_token_key + accessToken.getTokenValue(), refreshToken.getTokenValue(), getSec(refreshToken.getExpiresAt()));

            return ResponseEntity.ok(accessToken);
        } else {
            return ResponseEntity.status(500).body("feign client exec fail");
        }
    }


    @GetMapping("/userinfo")
    public ResponseEntity<?> userinfo(@RequestHeader("Authorization") String token) {
        return oAuthFeignClient.userinfo(token);
    }


    private static long getSec(Instant instant) {
        Duration duration = Duration.between(Instant.now(), instant);
        return duration.getSeconds();
    }

}
