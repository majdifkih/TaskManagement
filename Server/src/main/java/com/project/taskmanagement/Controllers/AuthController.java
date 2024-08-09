package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Auth.AuthService;
import com.project.taskmanagement.payload.request.LoginRequest;
import com.project.taskmanagement.payload.request.SignupRequest;
import com.project.taskmanagement.payload.request.TokenRefreshRequest;
import com.project.taskmanagement.payload.response.JwtResponse;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.payload.response.TokenRefreshResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


  @Autowired
  AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@RequestBody SignupRequest signUpRequest) {

    return authService.registerUser(signUpRequest);

  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<TokenRefreshResponse> refreshtoken(@RequestBody TokenRefreshRequest request) {
    return authService.refreshtoken(request);
  }

  @PostMapping("/signout")
  public ResponseEntity<MessageResponse> logoutUser() {
    return authService.logoutUser();
  }
}
