package com.customerservice.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.customerservice.authentication.entity.User;
import com.customerservice.authentication.repository.UserRepository;
import com.customerservice.authentication.service.UserService;
import com.customerservice.authentication.dto.SignUpRequestDto;
import com.customerservice.authentication.dto.SignUpResponseDto;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> registerUser(@RequestBody SignUpRequestDto signUpRequest) {

        return ResponseEntity.ok().body(userService.registerUser(signUpRequest));
    }    
} 