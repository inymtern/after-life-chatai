package com.after.life.chatai.controller;

import com.after.life.chatai.dto.BlogInsertDTO;
import com.after.life.chatai.remote.BlogOAuthFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-25 17:19
 */
@RestController
@RequestMapping("/oauth2/resource")
public class OAuth2ResourceController {

    private final BlogOAuthFeignClient blogOAuthFeignClient;

    public OAuth2ResourceController(BlogOAuthFeignClient blogOAuthFeignClient) {
        this.blogOAuthFeignClient = blogOAuthFeignClient;
    }

    @PostMapping("/syncBlog")
    public ResponseEntity<?> syncBlog(@RequestHeader("Authorization") String token, @RequestBody BlogInsertDTO insertDTO) {
        return blogOAuthFeignClient.blogSync(token, insertDTO);
    }
}
