package com.example.springauth.repository;

import com.example.springauth.domain.user.UserEntity;

import java.util.Optional;

public interface UserRepository {
    void save(UserEntity user);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<UserEntity> findById(Long id);
}
