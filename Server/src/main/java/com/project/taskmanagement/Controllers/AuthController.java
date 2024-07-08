package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.AuthService;
import com.project.taskmanagement.payload.request.LoginRequest;
import com.project.taskmanagement.payload.request.SignupRequest;
import com.project.taskmanagement.payload.request.TokenRefreshRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


  @Autowired
  AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

    return authService.registerUser(signUpRequest);

  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
    return authService.refreshtoken(request);
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    return authService.logoutUser();
  }
}
