package com.after.life.chatai.remote;

import com.after.life.chatai.dto.*;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 20:04
 */
@FeignClient(name = "blogApp", path = "/blog")
public interface BlogOAuthFeignClient {

//    @GetMapping("/oauth2/authorize")
//    ResponseEntity<?> fetchCode(OAuth2CodeRequest request);

    @PostMapping("/oauth2/access-token")
    ResponseEntity<Oauth2TokenDTO> fetchAccessToken(@SpringQueryMap OAuth2AccessTokenRequest request);

    @GetMapping("/oauth2/resource/userinfo")
    ResponseEntity<OAuth2UserInfoDTO> userinfo(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("/oauth2/refresh-token")
    ResponseEntity<Oauth2TokenDTO> fetchRefreshToken(@SpringQueryMap OAuth2RefreshTokenRequest request);

    @PostMapping("/oauth2/resource/blogSync")
    ResponseEntity<String> blogSync(@RequestHeader("Authorization") String bearToken, @RequestBody BlogInsertDTO insertDTO);
}
