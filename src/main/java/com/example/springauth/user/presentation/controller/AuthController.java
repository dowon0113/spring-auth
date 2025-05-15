package com.example.springauth.user.presentation.controller;

import com.example.springauth.user.application.dto.request.ReqLoginDTO;
import com.example.springauth.user.application.dto.request.ReqUserPostDTO;
import com.example.springauth.user.application.dto.response.ResLoginDTO;
import com.example.springauth.user.application.dto.response.ResUserPostDTO;
import com.example.springauth.user.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ResUserPostDTO> createUser(
            @RequestBody ReqUserPostDTO dto) {
        ResUserPostDTO response = authService.save(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(
            @RequestBody ReqLoginDTO dto) {
        ResLoginDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
