package com.example.springauth.controller;

import com.example.springauth.dto.request.ReqUserPostDTO;
import com.example.springauth.dto.response.ResUserPostDTO;
import com.example.springauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResUserPostDTO> createUser(
            @RequestBody ReqUserPostDTO dto) {
        ResUserPostDTO response = userService.save(dto);
        return ResponseEntity.ok(response);
    }
}
