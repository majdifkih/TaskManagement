package com.project.taskmanagement.Services.Profil;

import com.project.taskmanagement.Entities.Role;
import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.Map.ProfilMapper;
import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilServiceImp implements ProfilService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfilMapper profilMapper;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public ResponseEntity<List<ProfilDto>> getEmployeesUsers() {
        List<User> users = userRepository.findByRole(Role.ROLE_EMPLOYEE);
        List<ProfilDto> profilDtos = profilMapper.toDtoList(users);
        return ResponseEntity.ok(profilDtos);
    }


    @Override
    public ResponseEntity<MessageResponse> updateProfile(ProfilDto profilDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            if (profilDto.getUsername() != null) {
                existingUser.setUsername(profilDto.getUsername());
            }
            if (profilDto.getEmail() != null) {
                existingUser.setEmail(profilDto.getEmail());
            }
            if (profilDto.getPassword() != null) {
                existingUser.setPassword(encoder.encode(profilDto.getPassword()));
            }

            userRepository.save(existingUser);
            return ResponseEntity.ok(new MessageResponse("Profil updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }
    }
    @Override
    public ResponseEntity<ProfilDto> getUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            ProfilDto profilDto = profilMapper.toDto(user.get());
            return ResponseEntity.ok(profilDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }




//    @Override
//    public ResponseEntity<MessageResponse> deleteUser(Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String authenticatedUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
//
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User userToDelete = optionalUser.get();
//
//
//            if (userToDelete.getUsername().equals(authenticatedUsername)) {
//
//                userRepository.deleteById(userId);
//
//                SecurityContextHolder.clearContext();
//
//                return ResponseEntity.ok(new MessageResponse("Your account has been deleted successfully."));
//            } else {
//                // Supprimer l'utilisateur sans déconnexion
//                userRepository.deleteById(userId);
//                return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new MessageResponse("User not found"));
//        }
//    }


}
