package com.example.springauth.controller;

import com.example.springauth.dto.request.ReqLoginDTO;
import com.example.springauth.dto.request.ReqUserPostDTO;
import com.example.springauth.dto.response.ResLoginDTO;
import com.example.springauth.dto.response.ResUserPostDTO;
import com.example.springauth.dto.response.ResUserRolePatchDTO;
import com.example.springauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @PatchMapping("/admin/users/{userId}/roles")
    @PreAuthorize("hasAuthority('AMDIN')")
    public ResponseEntity<ResUserRolePatchDTO> grantAdminRole(
            @PathVariable Long userId){
        ResUserRolePatchDTO response = userService.grantAdminRole(userId);
        return ResponseEntity.ok(response);
    }
}
