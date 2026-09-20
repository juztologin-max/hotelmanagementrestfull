package com.hma.api;

import org.springframework.web.bind.annotation.RestController;

import com.hma.api.profile.CustomerProfile;
import com.hma.api.users.LoginUser;
import com.hma.api.users.UserService;
import com.hma.api.users.userroles.UserRole;
import com.hma.api.users.userroles.UserRolesService;
import com.hma.api.users.userroles.UserRole.UserRolesEnum;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/customers")
public class CustomerApiController {
    private final UserService userService;

    public CustomerApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-customer")
    ResponseEntity<CustomerProfileReturnedDTO> newCustomer(@RequestBody CustomerProfileDTO customerDetails) {
        System.out.println(customerDetails.toString());
        CustomerProfile profile = CustomerProfile.builder(customerDetails.fullname())
                .setAadhar(customerDetails.aadhar()).setAddress(customerDetails.address())
                .setPhone(customerDetails.phone()).setEmail(customerDetails.email()).setDob(customerDetails.dob())
                .build();

        LoginUser user = LoginUser.builder(customerDetails.username(), customerDetails.password())
                .addRole(new UserRole(UserRolesEnum.ROLE_CUSTOMER))
                .build();
        user.setProfile(profile);
        CustomerProfileReturnedDTO customerProfileReturnedDTO = new CustomerProfileReturnedDTO(profile.getFullname(),
                profile.getDob(), profile.getAadhar(), profile.getAddress());
        userService.saveUser(user);
        return ResponseEntity.ok(customerProfileReturnedDTO);

    }
}

record CustomerProfileDTO(String username, String password, String fullname, LocalDateTime dob, String aadhar,
        String address, String phone, String email) {
}

record CustomerProfileReturnedDTO(String fullname, LocalDateTime dob, String aadhar,
        String address) {
}
