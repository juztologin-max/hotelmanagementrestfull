package com.hma.api.users.userroles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hma.api.users.userroles.UserRole.UserRolesEnum;

public interface UserRolesRepo extends JpaRepository<UserRole, Long> {
    @Override
    public Optional<UserRole> findById(Long id);

    public boolean existsByRole(UserRolesEnum role);

    public Optional<UserRole> getByRole(UserRolesEnum role);
}
