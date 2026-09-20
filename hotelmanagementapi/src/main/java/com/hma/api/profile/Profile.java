package com.hma.api.profile;

import java.time.LocalDateTime;

import com.hma.api.users.LoginUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity(name = "profiles")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Profile {
    @Id
    private Long Id;
    @Column(nullable = false)
    private String fullname;
    @Column(nullable = false)
    private LocalDateTime dob;
    @Column(nullable = false, unique = true)
    private String aadhar;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private LoginUser user;

    protected Profile(String fullname, LocalDateTime dob, String aadhar, String address, String phone, String email) {
        this.fullname = fullname;
        this.dob = dob;
        this.aadhar = aadhar;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return Id;
    }

    public String getFullname() {
        return fullname;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public String getAadhar() {
        return aadhar;
    }

    public String getAddress() {
        return address;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public static abstract class ProfileBuilder<T extends ProfileBuilder<T>> {
        protected String fullname;
        protected LocalDateTime dob;
        protected String aadhar;
        protected String address;
        protected String phone;
        protected String email;

        protected abstract T self();

        public ProfileBuilder(String fullname) {
            this.fullname = fullname;
        }

        public T setDob(LocalDateTime dob) {
            this.dob = dob;
            return self();
        }

        public T setAadhar(String aadhar) {
            this.aadhar = aadhar;
            return self();

        }

        public T setEmail(String email) {
            this.email = email;
            return self();

        }

        public T setAddress(String address) {
            this.address = address;
            return self();
        }

        public T setPhone(String phone) {
            this.phone = phone;
            return self();
        }

        public abstract Profile build();
    }

}
