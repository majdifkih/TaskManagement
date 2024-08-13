package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Profil.ProfilService;
import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/profil")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @GetMapping("/employees")
    public ResponseEntity<List<ProfilDto>> getEmployeesUsers() {
        return profilService.getEmployeesUsers();
    }


    @GetMapping("/userdetail")
    public ResponseEntity<ProfilDto> getUserDetail() {
        return profilService.getUserDetail();
    }

    @GetMapping("/userbyusername")
    public ResponseEntity<ProfilDto> getUserByUsername( @RequestParam() String username) {
        return profilService.getUserByUsername(username);
    }

    @PutMapping("/updateuser")
    public ResponseEntity<MessageResponse> updateProfile(@RequestBody ProfilDto profilDto) {
        return profilService.updateProfile(profilDto);
    }


//    @DeleteMapping("/deluser/{userId}")
//    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long userId) {
//        return profilService.deleteUser(userId);
//    }
}
