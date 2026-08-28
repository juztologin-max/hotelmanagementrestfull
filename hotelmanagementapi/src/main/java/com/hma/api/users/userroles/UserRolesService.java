package com.hma.api.users.userroles;

import org.springframework.stereotype.Service;

@Service
public class UserRolesService {

	private final UserRolesRepo repo;

	public UserRolesService(UserRolesRepo repo) {
		this.repo = repo;
	}

	public UserRoles findById(Long id) throws Exception {
		return repo.findById(id).orElseThrow(() -> new Exception("Role with id '" + id + "' not found"));

	}

}
