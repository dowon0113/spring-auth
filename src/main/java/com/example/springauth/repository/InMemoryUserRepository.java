package com.example.springauth.repository;

import com.example.springauth.domain.user.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, UserEntity> userInfo = new HashMap<>();
    private final Map<Long, UserEntity> userById = new HashMap<>();


    @Override
    public void save(UserEntity user) {
        userInfo.put(user.getUsername(), user);
        userById.put(user.getId(), user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(userInfo.get(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userInfo.containsKey(username);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(userById.get(id));
    }
}
