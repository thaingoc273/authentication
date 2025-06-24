package com.customerservice.authentication.controller;

import com.customerservice.authentication.dto.UserResponseDto;
import com.customerservice.authentication.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
  final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> response = userService.getAllUsers();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  public ResponseEntity<UserResponseDto> getUserProfile(@RequestParam String username) {
    try {
      UserResponseDto response = userService.findByUsernameWithRoles(username);
      return ResponseEntity.ok(response);
    } catch (SecurityException e) {
      throw new AccessDeniedException("You are not authorized to access this user's information.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("User not found with username: " + username);
    }
  }
}
