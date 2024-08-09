package com.project.taskmanagement.Services.Auth;

import com.project.taskmanagement.Entities.RefreshToken;
import com.project.taskmanagement.Entities.Role;
import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.request.LoginRequest;
import com.project.taskmanagement.payload.request.SignupRequest;
import com.project.taskmanagement.payload.request.TokenRefreshRequest;
import com.project.taskmanagement.payload.response.JwtResponse;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.payload.response.TokenRefreshResponse;
import com.project.taskmanagement.repository.Auth.UserRepository;
import com.project.taskmanagement.security.jwt.JwtUtils;
import com.project.taskmanagement.security.services.RefreshTokenService;
import com.project.taskmanagement.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);
            String role = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User has no roles assigned"));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), role));

    }


    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()), Role.valueOf(signUpRequest.getRole()));

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<TokenRefreshResponse> refreshtoken(TokenRefreshRequest request) {
            String requestRefreshToken = request.getRefreshToken();

            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshToken -> {
                        refreshTokenService.verifyExpiration(refreshToken);
                        User user = refreshToken.getUser();
                        String role = user.getRole().name();
                        String token = jwtUtils.generateTokenFromUsername(user.getId(), user.getUsername(), user.getEmail(), role);
                        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token is not valid or expired."));
    }

    @Override
    public ResponseEntity<MessageResponse> logoutUser() {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
            return ResponseEntity.ok(new MessageResponse("Log out successful!"));

    }

}
