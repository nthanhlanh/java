package com.alibou.security.controller;

import com.alibou.security.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alibou.security.dto.ChangePasswordRequest;
import com.alibou.security.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<ResponseEntity.BodyBuilder> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public UserDto getUserById(UUID id){
        return  service.getUserById(id);
    }
}
