package com.customerservice.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.customerservice.authentication.dto.SignUpRequestDto;
import com.customerservice.authentication.dto.SignUpResponseDto;
import com.customerservice.authentication.entity.User;
import com.customerservice.authentication.repository.UserRepository;
import com.customerservice.authentication.repository.RoleRepository;
import com.customerservice.authentication.entity.Role;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.customerservice.authentication.dto.LoginRequestDto;
import com.customerservice.authentication.security.JwtUtil;

@Service
public class RegistrationService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public SignUpResponseDto registerUser(SignUpRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword())); // Hash in real apps!
        user.setEmail(signUpRequest.getEmail());

        // Assign roles
        Set<Role> roles = new HashSet<>();
        if (signUpRequest.getRoles() != null) {
            for (var roleDto : signUpRequest.getRoles()) {
                roleRepository.findByRoleCode(roleDto.getRoleCode())
                    .ifPresent(roles::add);
            }
        }
        user.setRoles(roles);

        userRepository.save(user);
        // Prepare response
        Set<String> roleCodes = roles.stream().map(Role::getRoleCode).collect(Collectors.toSet());
        return new SignUpResponseDto(user.getUsername(), user.getEmail(), roleCodes);
    }

    public String loginUser(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }        
        return jwtUtil.generateToken(user.getUsername(), user.getRoles().stream().map(Role::getRoleCode).collect(Collectors.toSet()));
    }
}