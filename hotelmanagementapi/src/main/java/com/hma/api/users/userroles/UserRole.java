package com.hma.api.users.userroles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "user_roles")
public class UserRole {
    public enum UserRolesEnum {
        ROLE_ADMIN, ROLE_STAFF, ROLE_CUSTOMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // @Column(name = "role_name", columnDefinition =
    // "ENUM('ROLE_ADMIN','ROLE_STAFF','ROLE_CUSTOMER')", nullable = false, length =
    // 20)
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, length = 20)
    UserRolesEnum role;

    public UserRole() {
    }

    public UserRole(UserRolesEnum role) {
        this.role = role;
    }

    public UserRole(String user) {
        this(UserRolesEnum.valueOf(user));
    }

    public UserRolesEnum getRole() {
        return role;
    }

    public void setRole(UserRolesEnum role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
