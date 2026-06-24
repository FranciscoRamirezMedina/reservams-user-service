package com.duoc.reservams.userservice.service;

import com.duoc.reservams.userservice.dto.UserProfileRequestDTO;
import com.duoc.reservams.userservice.dto.UserProfileResponseDTO;
import com.duoc.reservams.userservice.model.UserProfile;
import com.duoc.reservams.userservice.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// pruebas unitarias para UserProfileService
@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    void findAll_shouldReturnUserProfiles() {
        // Given
        when(userProfileRepository.findAll()).thenReturn(List.of(
                buildUserProfile(1L, 1L, "CLIENTE"),
                buildUserProfile(2L, 2L, "ADMIN")
        ));

        // When
        List<UserProfileResponseDTO> response = userProfileService.findAll();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());

        verify(userProfileRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnUserProfile_whenExists() {
        // Given
        UserProfile profile = buildUserProfile(1L, 1L, "CLIENTE");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        // When
        UserProfileResponseDTO response = userProfileService.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAuthUserId());
        assertEquals("Francisco", response.getFirstName());
        assertEquals("CLIENTE", response.getRoleName());

        verify(userProfileRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenUserProfileNotFound() {
        // Given
        when(userProfileRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.findById(99L)
        );

        // Then
        assertEquals("Perfil de usuario no encontrado", exception.getMessage());

        verify(userProfileRepository, times(1)).findById(99L);
    }

    @Test
    void findByAuthUserId_shouldReturnUserProfile_whenExists() {
        // Given
        UserProfile profile = buildUserProfile(1L, 10L, "CLIENTE");

        when(userProfileRepository.findByAuthUserId(10L)).thenReturn(Optional.of(profile));

        // When
        UserProfileResponseDTO response = userProfileService.findByAuthUserId(10L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10L, response.getAuthUserId());
        assertEquals("CLIENTE", response.getRoleName());

        verify(userProfileRepository, times(1)).findByAuthUserId(10L);
    }

    @Test
    void findByAuthUserId_shouldThrowException_whenUserProfileNotFound() {
        // Given
        when(userProfileRepository.findByAuthUserId(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.findByAuthUserId(99L)
        );

        // Then
        assertEquals("Perfil no encontrado para el usuario autenticado", exception.getMessage());

        verify(userProfileRepository, times(1)).findByAuthUserId(99L);
    }

    @Test
    void findByRoleName_shouldReturnUserProfiles() {
        // Given
        when(userProfileRepository.findByRoleName("CLIENTE")).thenReturn(List.of(
                buildUserProfile(1L, 1L, "CLIENTE"),
                buildUserProfile(2L, 2L, "CLIENTE")
        ));

        // When
        List<UserProfileResponseDTO> response = userProfileService.findByRoleName("CLIENTE");

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("CLIENTE", response.get(0).getRoleName());

        verify(userProfileRepository, times(1)).findByRoleName("CLIENTE");
    }

    @Test
    void create_shouldCreateUserProfile_whenAuthUserIdDoesNotExist() {
        // Given
        UserProfileRequestDTO request = buildUserProfileRequest("CLIENTE");

        when(userProfileRepository.existsByAuthUserId(1L)).thenReturn(false);

        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile profile = invocation.getArgument(0);
            profile.setId(1L);
            return profile;
        });

        // When
        UserProfileResponseDTO response = userProfileService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAuthUserId());
        assertEquals("Francisco", response.getFirstName());
        assertEquals("Andrade", response.getLastName());
        assertEquals("CLIENTE", response.getRoleName());
        assertNotNull(response.getCreatedAt());

        verify(userProfileRepository, times(1)).existsByAuthUserId(1L);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void create_shouldThrowException_whenAuthUserIdAlreadyExists() {
        // Given
        UserProfileRequestDTO request = buildUserProfileRequest("CLIENTE");

        when(userProfileRepository.existsByAuthUserId(1L)).thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.create(request)
        );

        // Then
        assertEquals("Ya existe un perfil para este usuario", exception.getMessage());

        verify(userProfileRepository, times(1)).existsByAuthUserId(1L);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void update_shouldUpdateUserProfile_whenExists() {
        // Given
        UserProfile profile = buildUserProfile(1L, 1L, "CLIENTE");
        UserProfileRequestDTO request = buildUserProfileRequest("ADMIN");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile updatedProfile = invocation.getArgument(0);
            return updatedProfile;
        });

        // When
        UserProfileResponseDTO response = userProfileService.update(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Francisco", response.getFirstName());
        assertEquals("Andrade", response.getLastName());
        assertEquals("ADMIN", response.getRoleName());

        verify(userProfileRepository, times(1)).findById(1L);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void update_shouldThrowException_whenUserProfileNotFound() {
        // Given
        UserProfileRequestDTO request = buildUserProfileRequest("ADMIN");

        when(userProfileRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.update(99L, request)
        );

        // Then
        assertEquals("Perfil de usuario no encontrado", exception.getMessage());

        verify(userProfileRepository, times(1)).findById(99L);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void delete_shouldDeleteUserProfile_whenExists() {
        // Given
        when(userProfileRepository.existsById(1L)).thenReturn(true);

        // When
        userProfileService.delete(1L);

        // Then
        verify(userProfileRepository, times(1)).existsById(1L);
        verify(userProfileRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenUserProfileNotFound() {
        // Given
        when(userProfileRepository.existsById(99L)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.delete(99L)
        );

        // Then
        assertEquals("Perfil de usuario no encontrado", exception.getMessage());

        verify(userProfileRepository, times(1)).existsById(99L);
        verify(userProfileRepository, never()).deleteById(anyLong());
    }

    private UserProfileRequestDTO buildUserProfileRequest(String roleName) {
        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setAuthUserId(1L);
        request.setFirstName("Francisco");
        request.setLastName("Andrade");
        request.setPhone("+56912345678");
        request.setDocumentNumber("12345678-9");
        request.setRoleName(roleName);
        return request;
    }

    private UserProfile buildUserProfile(Long id, Long authUserId, String roleName) {
        UserProfile profile = new UserProfile();
        profile.setId(id);
        profile.setAuthUserId(authUserId);
        profile.setFirstName("Francisco");
        profile.setLastName("Andrade");
        profile.setPhone("+56912345678");
        profile.setDocumentNumber("12345678-9");
        profile.setRoleName(roleName);
        profile.setCreatedAt(LocalDateTime.now());
        return profile;
    }
}