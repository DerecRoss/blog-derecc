package br.com.blog.derecc.dev.user.controller;

import br.com.blog.derecc.dev.security.service.AuthService;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserAuthorResponse> getMe() {

        User user =
                authService.getAuthenticatedUser();

        UserAuthorResponse response =
                new UserAuthorResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAvatarUrl(),
                        user.getUserRole()
                );

        return ResponseEntity.ok(response);
    }
}
