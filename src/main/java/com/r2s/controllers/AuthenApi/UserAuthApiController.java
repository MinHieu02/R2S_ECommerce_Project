package com.r2s.controllers.AuthenApi;

import com.r2s.dtos.common.*;
import com.r2s.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthApiController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<RegisterResponseDTO>> register(@RequestBody RegisterRequestDTO userDto) {
        return ResponseEntity.ok(userServiceImpl.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO userDto) {
        return ResponseEntity.ok(userServiceImpl.login(userDto));
    }
}

