package com.duoc.reservams.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO usado para crear o actualizar un perfil de usuario
@Data
public class UserProfileRequestDTO {

    @NotNull(message = "El authUserId es obligatorio")
    private Long authUserId;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private String phone;

    private String documentNumber;

    @NotBlank(message = "El rol es obligatorio")
    private String roleName;
}