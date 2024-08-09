package com.project.taskmanagement.Services.Auth;

import com.project.taskmanagement.payload.request.LoginRequest;
import com.project.taskmanagement.payload.request.SignupRequest;
import com.project.taskmanagement.payload.request.TokenRefreshRequest;
import com.project.taskmanagement.payload.response.JwtResponse;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.payload.response.TokenRefreshResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest);
    ResponseEntity<TokenRefreshResponse> refreshtoken(TokenRefreshRequest request);
    ResponseEntity<MessageResponse> logoutUser();
}
