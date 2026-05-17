package com.duoc.reservams.userservice.repository;

import com.duoc.reservams.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// repository permite trabajar con la tabla user_profiles
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // busca un perfil por el ID del usuario de auth-service
    Optional<UserProfile> findByAuthUserId(Long authUserId);

    // valida si ya existe un perfil para ese usuario autenticado
    boolean existsByAuthUserId(Long authUserId);

    // lista perfiles segun el rol
    List<UserProfile> findByRoleName(String roleName);
}