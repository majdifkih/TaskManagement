package com.project.taskmanagement.Services;

import com.project.taskmanagement.payload.request.LoginRequest;
import com.project.taskmanagement.payload.request.SignupRequest;
import com.project.taskmanagement.payload.request.TokenRefreshRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
    ResponseEntity<?> refreshtoken(TokenRefreshRequest request);
    ResponseEntity<?> logoutUser();
}
