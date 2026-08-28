package com.hma.api.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<LoginUser, Long> {
    public Optional<LoginUser> findByUsername(String username);

}
