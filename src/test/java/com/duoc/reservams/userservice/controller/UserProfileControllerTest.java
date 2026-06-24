package com.duoc.reservams.userservice.controller;

import com.duoc.reservams.userservice.dto.UserProfileRequestDTO;
import com.duoc.reservams.userservice.dto.UserProfileResponseDTO;
import com.duoc.reservams.userservice.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// pruebas unitarias para UserProfileController
@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    @Test
    void findAll_shouldReturnUserProfiles() {
        // Given
        when(userProfileService.findAll()).thenReturn(List.of(
                buildUserProfileResponse(1L, 1L, "CLIENTE"),
                buildUserProfileResponse(2L, 2L, "ADMIN")
        ));

        // When
        ResponseEntity<List<UserProfileResponseDTO>> response = userProfileController.findAll();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(userProfileService, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnUserProfile() {
        // Given
        when(userProfileService.findById(1L)).thenReturn(
                buildUserProfileResponse(1L, 1L, "CLIENTE")
        );

        // When
        ResponseEntity<UserProfileResponseDTO> response = userProfileController.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("CLIENTE", response.getBody().getRoleName());

        verify(userProfileService, times(1)).findById(1L);
    }

    @Test
    void findByAuthUserId_shouldReturnUserProfile() {
        // Given
        when(userProfileService.findByAuthUserId(10L)).thenReturn(
                buildUserProfileResponse(1L, 10L, "CLIENTE")
        );

        // When
        ResponseEntity<UserProfileResponseDTO> response = userProfileController.findByAuthUserId(10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getAuthUserId());
        assertEquals("CLIENTE", response.getBody().getRoleName());

        verify(userProfileService, times(1)).findByAuthUserId(10L);
    }

    @Test
    void findByRoleName_shouldReturnUserProfiles() {
        // Given
        when(userProfileService.findByRoleName("CLIENTE")).thenReturn(List.of(
                buildUserProfileResponse(1L, 1L, "CLIENTE"),
                buildUserProfileResponse(2L, 2L, "CLIENTE")
        ));

        // When
        ResponseEntity<List<UserProfileResponseDTO>> response = userProfileController.findByRoleName("CLIENTE");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("CLIENTE", response.getBody().get(0).getRoleName());

        verify(userProfileService, times(1)).findByRoleName("CLIENTE");
    }

    @Test
    void create_shouldReturnCreatedUserProfile() {
        // Given
        UserProfileRequestDTO request = buildUserProfileRequest("CLIENTE");

        when(userProfileService.create(request)).thenReturn(
                buildUserProfileResponse(1L, 1L, "CLIENTE")
        );

        // When
        ResponseEntity<UserProfileResponseDTO> response = userProfileController.create(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("CLIENTE", response.getBody().getRoleName());

        verify(userProfileService, times(1)).create(request);
    }

    @Test
    void update_shouldReturnUpdatedUserProfile() {
        // Given
        UserProfileRequestDTO request = buildUserProfileRequest("ADMIN");

        when(userProfileService.update(1L, request)).thenReturn(
                buildUserProfileResponse(1L, 1L, "ADMIN")
        );

        // When
        ResponseEntity<UserProfileResponseDTO> response = userProfileController.update(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("ADMIN", response.getBody().getRoleName());

        verify(userProfileService, times(1)).update(1L, request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // Given
        doNothing().when(userProfileService).delete(1L);

        // When
        ResponseEntity<Void> response = userProfileController.delete(1L);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(userProfileService, times(1)).delete(1L);
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

    private UserProfileResponseDTO buildUserProfileResponse(Long id, Long authUserId, String roleName) {
        return new UserProfileResponseDTO(
                id,
                authUserId,
                "Francisco",
                "Andrade",
                "+56912345678",
                "12345678-9",
                roleName,
                LocalDateTime.now()
        );
    }
}