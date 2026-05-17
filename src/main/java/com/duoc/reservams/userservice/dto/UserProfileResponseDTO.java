package com.duoc.reservams.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// DTO usado para responder datos del perfil sin exponer directamente la entidad
@Data
@AllArgsConstructor
public class UserProfileResponseDTO {

    private Long id;
    private Long authUserId;
    private String firstName;
    private String lastName;
    private String phone;
    private String documentNumber;
    private String roleName;
    private LocalDateTime createdAt;
}