package com.example.springauth.controller;

import com.example.springauth.dto.request.ReqLoginDTO;
import com.example.springauth.dto.request.ReqUserPostDTO;
import com.example.springauth.dto.response.ResLoginDTO;
import com.example.springauth.dto.response.ResUserPostDTO;
import com.example.springauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResUserPostDTO> createUser(
            @RequestBody ReqUserPostDTO dto) {
        ResUserPostDTO response = userService.save(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(
            @RequestBody ReqLoginDTO dto) {
        ResLoginDTO response = userService.login(dto);
        return ResponseEntity.ok(response);
    }



}
