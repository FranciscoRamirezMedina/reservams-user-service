package com.duoc.reservams.userservice.service;

import com.duoc.reservams.userservice.dto.UserProfileRequestDTO;
import com.duoc.reservams.userservice.dto.UserProfileResponseDTO;
import com.duoc.reservams.userservice.model.UserProfile;
import com.duoc.reservams.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio del perfil de usuario
@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfileResponseDTO> findAll() {
        logger.info("Listando todos los perfiles de usuario");

        List<UserProfileResponseDTO> profiles = userProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        logger.info("Se encontraron {} perfiles de usuario", profiles.size());

        return profiles;
    }

    public UserProfileResponseDTO findById(Long id) {
        logger.info("Buscando perfil de usuario por ID {}", id);

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontro perfil de usuario con ID {}", id);
                    return new RuntimeException("Perfil de usuario no encontrado");
                });

        logger.info("Perfil de usuario encontrado con ID {}", profile.getId());

        return toResponseDTO(profile);
    }

    public UserProfileResponseDTO findByAuthUserId(Long authUserId) {
        logger.info("Buscando perfil por authUserId {}", authUserId);

        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> {
                    logger.warn("No se encontro perfil para authUserId {}", authUserId);
                    return new RuntimeException("Perfil no encontrado para el usuario autenticado");
                });

        logger.info("Perfil encontrado para authUserId {} con perfil ID {}", authUserId, profile.getId());

        return toResponseDTO(profile);
    }

    public List<UserProfileResponseDTO> findByRoleName(String roleName) {
        logger.info("Listando perfiles por rol {}", roleName);

        List<UserProfileResponseDTO> profiles = userProfileRepository.findByRoleName(roleName)
                .stream()
                .map(this::toResponseDTO)
                .toList();

        logger.info("Se encontraron {} perfiles con rol {}", profiles.size(), roleName);

        return profiles;
    }

    public UserProfileResponseDTO create(UserProfileRequestDTO request) {
        logger.info("Iniciando creacion de perfil para authUserId {}", request.getAuthUserId());

        if (userProfileRepository.existsByAuthUserId(request.getAuthUserId())) {
            logger.warn("No se pudo crear perfil. Ya existe un perfil para authUserId {}", request.getAuthUserId());
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

        logger.info("Guardando perfil para authUserId {} con rol {}", profile.getAuthUserId(), profile.getRoleName());

        UserProfile savedProfile = userProfileRepository.save(profile);

        logger.info("Perfil creado correctamente con ID {} para authUserId {}", savedProfile.getId(), savedProfile.getAuthUserId());

        return toResponseDTO(savedProfile);
    }

    public UserProfileResponseDTO update(Long id, UserProfileRequestDTO request) {
        logger.info("Iniciando actualizacion de perfil con ID {}", id);

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo actualizar. Perfil de usuario no encontrado con ID {}", id);
                    return new RuntimeException("Perfil de usuario no encontrado");
                });

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setDocumentNumber(request.getDocumentNumber());
        profile.setRoleName(request.getRoleName());

        logger.info("Guardando cambios del perfil con ID {}", id);

        UserProfile updatedProfile = userProfileRepository.save(profile);

        logger.info("Perfil actualizado correctamente con ID {}", updatedProfile.getId());

        return toResponseDTO(updatedProfile);
    }

    public void delete(Long id) {
        logger.info("Iniciando eliminacion de perfil con ID {}", id);

        if (!userProfileRepository.existsById(id)) {
            logger.warn("No se pudo eliminar. Perfil de usuario no encontrado con ID {}", id);
            throw new RuntimeException("Perfil de usuario no encontrado");
        }

        userProfileRepository.deleteById(id);

        logger.info("Perfil eliminado correctamente con ID {}", id);
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