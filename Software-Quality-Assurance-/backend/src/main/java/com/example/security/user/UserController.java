package com.example.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDto>> getUserListWithPermissions() {
        List<UserDto> usersWithPermissions = service.getUserListWithPermissions();
        return ResponseEntity.ok(usersWithPermissions);
    }
}
