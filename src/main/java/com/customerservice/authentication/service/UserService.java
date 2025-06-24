package com.customerservice.authentication.service;

import com.customerservice.authentication.dto.UserResponseDto;
import com.customerservice.authentication.entity.User;
import com.customerservice.authentication.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(
            user ->
                new UserResponseDto(
                    user.getUsername(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getRoles().stream()
                        .map(role -> role.getRoleCode())
                        .collect(Collectors.toSet())))
        .collect(Collectors.toList());
  }

  @Transactional
  public UserResponseDto findByUsernameWithRoles(String username) {
    // Get the current authenticated username
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication != null ? authentication.getName() : null;

    User user =
        userRepository
            .findByUsernameWithRoles(username)
            .orElseThrow(
                () -> new IllegalArgumentException("User not found with username: " + username));

    User currentUser =
        userRepository
            .findByUsernameWithRoles(currentUsername)
            .orElseThrow(() -> new IllegalArgumentException("Authentication user not found"));

    if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleCode().equals("ADMIN"))) {
      return toUserResponseDto(user);
    }

    if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleCode().equals("MANAGER"))
        && (user.getRoles().stream().allMatch(role -> role.getRoleCode().equals("USER")))) {
      return toUserResponseDto(user);
    }

    if (currentUsername == null || !currentUsername.equals(username)) {
      throw new SecurityException("You are not authorized to access this user's information.");
    }

    return toUserResponseDto(user);
  }

  private UserResponseDto toUserResponseDto(User user) {
    return new UserResponseDto(
        user.getUsername(),
        user.getEmail(),
        user.getAddress(),
        user.getRoles().stream().map(role -> role.getRoleCode()).collect(Collectors.toSet()));
  }
}
