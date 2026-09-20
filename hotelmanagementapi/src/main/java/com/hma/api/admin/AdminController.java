package com.hma.api.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
// @CrossOrigin("") //for allowing react ui to access these endpoints
public class AdminController {
    @GetMapping("/test")
    public String getLoginTestHandler() {
        return "success";
    }

    @PostMapping("/test")
    public String postLoginTestdHandler(@RequestBody TestWithIdContainer idContainer) {
        return idContainer.id();
    }

}

record TestWithIdContainer(String id) {
}
