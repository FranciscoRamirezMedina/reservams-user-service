package com.duoc.reservams.userservice.service;

import com.duoc.reservams.userservice.dto.UserProfileRequestDTO;
import com.duoc.reservams.userservice.dto.UserProfileResponseDTO;
import com.duoc.reservams.userservice.model.UserProfile;
import com.duoc.reservams.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio del perfil de usuario
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfileResponseDTO> findAll() {
        return userProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserProfileResponseDTO findById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de usuario no encontrado"));

        return toResponseDTO(profile);
    }

    public UserProfileResponseDTO findByAuthUserId(Long authUserId) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para el usuario autenticado"));

        return toResponseDTO(profile);
    }

    public List<UserProfileResponseDTO> findByRoleName(String roleName) {
        return userProfileRepository.findByRoleName(roleName)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserProfileResponseDTO create(UserProfileRequestDTO request) {
        if (userProfileRepository.existsByAuthUserId(request.getAuthUserId())) {
            throw new RuntimeException("Ya existe un perfil para este usuario");
        }

        UserProfile profile = new UserProfile();
        profile.setAuthUserId(request.getAuthUserId());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setDocumentNumber(request.getDocumentNumber());
        profile.setRoleName(request.getRoleName());
        profile.setCreatedAt(LocalDateTime.now());

        UserProfile savedProfile = userProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public UserProfileResponseDTO update(Long id, UserProfileRequestDTO request) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de usuario no encontrado"));

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setDocumentNumber(request.getDocumentNumber());
        profile.setRoleName(request.getRoleName());

        UserProfile updatedProfile = userProfileRepository.save(profile);

        return toResponseDTO(updatedProfile);
    }

    public void delete(Long id) {
        if (!userProfileRepository.existsById(id)) {
            throw new RuntimeException("Perfil de usuario no encontrado");
        }

        userProfileRepository.deleteById(id);
    }

    // convierte una entidad en DTO de respuesta
    private UserProfileResponseDTO toResponseDTO(UserProfile profile) {
        return new UserProfileResponseDTO(
                profile.getId(),
                profile.getAuthUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getDocumentNumber(),
                profile.getRoleName(),
                profile.getCreatedAt()
        );
    }
}