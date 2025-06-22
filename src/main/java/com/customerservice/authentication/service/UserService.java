package com.customerservice.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.customerservice.authentication.entity.User;
import com.customerservice.authentication.repository.UserRepository;
import com.customerservice.authentication.dto.UserResponseDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserResponseDto(user.getUsername(), 
                                            user.getEmail(), 
                                            user.getAddress(), 
                                            user.getRoles().stream().map(role -> role.getRoleCode()).collect(Collectors.toSet())))
            .collect(Collectors.toList());
    }
}
