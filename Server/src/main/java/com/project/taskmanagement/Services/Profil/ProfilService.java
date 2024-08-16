package com.project.taskmanagement.Services.Profil;

import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProfilService {
    ResponseEntity<List<ProfilDto>> getEmployeesUsers();
    ResponseEntity<MessageResponse> updateProfile(ProfilDto profilDto);
    ResponseEntity<ProfilDto> getUserDetail();
   // ResponseEntity<MessageResponse> deleteUser(Long userId);
}
