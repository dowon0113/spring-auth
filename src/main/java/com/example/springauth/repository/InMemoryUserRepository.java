package com.example.springauth.repository;

import com.example.springauth.domain.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, UserEntity> userStore = new HashMap<>();

    @Override
    public void save(UserEntity user) {
        userStore.put(user.getUsername(), user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(userStore.get(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userStore.containsKey(username);
    }
}
