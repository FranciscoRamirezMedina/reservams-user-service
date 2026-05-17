package com.duoc.reservams.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// esta clase representa la tabla user_profiles en la base de datos
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    // ddentificador principal del perfil
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario que viene desde auth-service
    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Long authUserId;

    // nombre del usuario
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    // apellido del usuario
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    // telefono de contacto
    @Column(length = 30)
    private String phone;

    // documento del usuario, ej RUT o pasaporte
    @Column(name = "document_number", length = 30)
    private String documentNumber;

    // rol del usuario: ADMIN, OPERADOR o CLIENTE
    @Column(name = "role_name", nullable = false, length = 30)
    private String roleName;

    // fecha de creacion del perfil
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}