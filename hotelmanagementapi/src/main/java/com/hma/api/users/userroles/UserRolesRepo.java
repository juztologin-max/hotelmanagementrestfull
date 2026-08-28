package com.hma.api.users.userroles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hma.api.users.userroles.UserRoles.UserRolesEnum;

public interface UserRolesRepo extends JpaRepository<UserRoles, Long> {
	@Override
	public Optional<UserRoles> findById(Long id);

	public boolean existsByRole(UserRolesEnum role);

	public Optional<UserRoles> getByRole(UserRolesEnum role);
}
