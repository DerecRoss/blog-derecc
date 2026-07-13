package br.com.blog.derecc.dev.user.controller;

import br.com.blog.derecc.dev.security.service.AuthService;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.user.model.User;
import br.com.blog.derecc.dev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    private final UserService userService;

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

    @PostMapping("/me/avatar")
    public ResponseEntity<UserAuthorResponse> uploadAvatar(
            @RequestParam("file")
            MultipartFile file
    ) {

        User user =
                authService.getAuthenticatedUser();

        return ResponseEntity.ok(
                userService.updateAvatar(
                        file,
                        user
                )
        );
    }
}
