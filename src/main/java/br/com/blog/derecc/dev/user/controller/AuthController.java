package br.com.blog.derecc.dev.user.controller;

import br.com.blog.derecc.dev.user.dto.LoginRequest;
import br.com.blog.derecc.dev.user.dto.LoginResponse;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.user.dto.UserRegisterRequest;
import br.com.blog.derecc.dev.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(
            UserService service
    ) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthorResponse>
    register(
            @RequestBody UserRegisterRequest request
    ) {

        return ResponseEntity.ok(
                service.registerUser(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>
    login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                service.login(request)
        );
    }
}