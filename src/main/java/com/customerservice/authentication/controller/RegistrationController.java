package com.customerservice.authentication.controller;

import com.customerservice.authentication.dto.LoginRequestDto;
import com.customerservice.authentication.dto.SignUpRequestDto;
import com.customerservice.authentication.dto.SignUpResponseDto;
import com.customerservice.authentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

  @Autowired private RegistrationService registrationService;

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDto signUpRequest) {
    try {
      SignUpResponseDto response = registrationService.registerUser(signUpRequest);
      return ResponseEntity.ok().body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(new ErrorResponse("An error occurred during registration"));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) {
    try {
      String token = registrationService.loginUser(loginRequest);
      return ResponseEntity.ok().body(token);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(new ErrorResponse("An error during the login process occurred"));
    }
  }

  // Simple error response class
  public static class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
