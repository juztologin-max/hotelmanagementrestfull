package com.hma.api.users;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hma.api.users.userroles.UserRole;
import com.hma.api.users.userroles.UserRolesService;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo repo;
    private final UserRolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    protected UserService(UserRepo repo, UserRolesService rolesService, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(username + " is not present in the database");
        });
    }

    public boolean doesUsernameExist(String username) {
        return repo.existsByUsername(username);
    }

    public void saveUser(LoginUser user) {
        Set<UserRole> fixedRoles = user.roles.stream().map(r -> rolesService.findByRole(r.getRole().name()))
                .collect(Collectors.toSet());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(fixedRoles);
        repo.save(user);
    }

}
