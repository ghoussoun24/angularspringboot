package com.example.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TestUserController {

    @GetMapping("/test")
    public String sayhey(){
        return "Hello dear ADMIN ! cOngrats U have this PERMISSION";
    }
}
