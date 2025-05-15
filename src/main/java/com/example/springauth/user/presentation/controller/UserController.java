package com.example.springauth.user.presentation.controller;

import com.example.springauth.user.application.dto.response.ResUserRolePatchDTO;
import com.example.springauth.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("{userId}/roles")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ResUserRolePatchDTO> grantAdminRole(
            @PathVariable Long userId){
        ResUserRolePatchDTO response = userService.grantAdminRole(userId);
        return ResponseEntity.ok(response);
    }
}
