package com.hma.api.users.userroles;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Example;

@Service
public class UserRolesService {

    private final UserRolesRepo repo;

    public UserRolesService(UserRolesRepo repo) {
        this.repo = repo;
    }

    public UserRole findById(Long id) throws Exception {
        return repo.findById(id).orElseThrow(() -> new Exception("Role with id '" + id + "' not found"));

    }

    public UserRole findByRole(String sRole) {
        UserRole role = new UserRole(sRole.toUpperCase());
        Example<UserRole> roleCriteria = Example.of(role);

        return repo.findOne(roleCriteria).orElseThrow(() -> {
            throw new RuntimeException("Role[" + sRole + "]" + " not found");
        });
    }

}
