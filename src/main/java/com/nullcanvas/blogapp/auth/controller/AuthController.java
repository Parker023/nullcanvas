package com.nullcanvas.blogapp.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anirudh
 * @since 17/05/26
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/test")
    public String test() {
        return "null canvas App Up and Running";
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
