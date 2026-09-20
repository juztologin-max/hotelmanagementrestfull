package com.hma.api.profile;

import java.time.LocalDateTime;

public class CustomerProfile extends Profile {
    private CustomerProfile(String fullname, LocalDateTime dob, String aadhar, String address, String phone,
            String email) {
        super(fullname, dob, aadhar, address, phone, email);
    }

    public static CustomerProfileBuilder builder(String fullname) {
        return new CustomerProfileBuilder(fullname);
    }

    public static class CustomerProfileBuilder
            extends Profile.ProfileBuilder<CustomerProfileBuilder> {
        public CustomerProfileBuilder(String fullname) {
            super(fullname);
        }

        @Override
        public CustomerProfileBuilder self() {
            return this;
        }

        @Override
        public CustomerProfile build() {
            CustomerProfile profile = new CustomerProfile(this.fullname, this.dob, this.aadhar, this.address,
                    this.phone, this.email);
            return profile;
        }
    }

}
