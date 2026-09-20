package com.hma.api.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hma.api.profile.Profile;
import com.hma.api.users.userroles.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity(name = "login_user")
public class LoginUser implements UserDetails {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 80)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userdetails_userroles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<UserRole> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    Profile profile;

    protected LoginUser() {
    }

    private LoginUser(String username, String password, boolean enabled, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public static LoginUserBuilder builder(String username, String password) {
        return new LoginUserBuilder(username, password);
    }

    public static class LoginUserBuilder {
        private final String username;
        private final String password;
        private boolean enabled = true;
        private Set<UserRole> roles = new HashSet<>();

        public LoginUserBuilder(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public LoginUserBuilder enable() {
            this.enabled = true;
            return this;
        }

        public LoginUserBuilder disable() {
            this.enabled = false;
            return this;
        }

        public LoginUserBuilder addRole(UserRole role) {
            roles.add(role);
            return this;
        }

        public LoginUserBuilder addAllRoles(Set<UserRole> roles) {
            this.roles = roles;
            return this;
        }

        public LoginUser build() {
            return new LoginUser(username, password, enabled, roles);
        }

    }

    public Long getId() {
        return id;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        if (profile == null) {
            if (this.profile != null) {
                this.profile.setUser(null);
            }
        } else {
            profile.setUser(this);
        }
        this.profile = profile;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> r.getRole().name()).map(SimpleGrantedAuthority::new).toList();
    }

}
