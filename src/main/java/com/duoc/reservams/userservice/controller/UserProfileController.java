package com.duoc.reservams.userservice.controller;

import com.duoc.reservams.userservice.dto.UserProfileRequestDTO;
import com.duoc.reservams.userservice.dto.UserProfileResponseDTO;
import com.duoc.reservams.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador REST para manejar perfiles de usuario
@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // lista todos los perfiles
    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    // busca un perfil por su ID interno
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.findById(id));
    }

    // busca el perfil usando el ID que viene del auth-service
    @GetMapping("/auth/{authUserId}")
    public ResponseEntity<UserProfileResponseDTO> findByAuthUserId(@PathVariable Long authUserId) {
        return ResponseEntity.ok(userProfileService.findByAuthUserId(authUserId));
    }

    // lista perfiles segun rol, ej CLIENTE u OPERADOR
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserProfileResponseDTO>> findByRoleName(@PathVariable String roleName) {
        return ResponseEntity.ok(userProfileService.findByRoleName(roleName));
    }

    // crea un perfil de usuario
    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> create(@Valid @RequestBody UserProfileRequestDTO request) {
        return ResponseEntity.ok(userProfileService.create(request));
    }

    // actualiza un perfil existente
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileRequestDTO request) {

        return ResponseEntity.ok(userProfileService.update(id, request));
    }

    // elimina un perfil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}