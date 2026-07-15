package br.com.blog.derecc.dev.user.service;

import br.com.blog.derecc.dev.images.service.FilesService;
import br.com.blog.derecc.dev.security.dto.LoginRequest;
import br.com.blog.derecc.dev.security.dto.LoginResponse;
import br.com.blog.derecc.dev.security.service.JwtService;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.user.dto.UserRegisterRequest;
import br.com.blog.derecc.dev.user.enums.UserRole;
import br.com.blog.derecc.dev.user.model.User;
import br.com.blog.derecc.dev.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FilesService filesService;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = Logger.getLogger(String.valueOf(UserService.class));

    public UserAuthorResponse registerUser(UserRegisterRequest userRegisterRequest){

        userRepository.findByEmail(userRegisterRequest.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeException();
                });
        User user = new User();

        user.setPassword(
                passwordEncoder.encode(
                        userRegisterRequest.getPassword()
                )
        );
        user.setEmail(userRegisterRequest.getEmail());
        user.setUsername(userRegisterRequest.getUsername());
        user.setUserRole(UserRole.USER);

        user = userRepository.save(user);

        UserAuthorResponse userAuthorResponse = new UserAuthorResponse();
        userAuthorResponse.setAvatarUrl(user.getAvatarUrl());
        userAuthorResponse.setEmail(user.getEmail());
        userAuthorResponse.setId(user.getId());
        userAuthorResponse.setUsername(user.getUsername());
        userAuthorResponse.setUserRole(user.getUserRole());

        return userAuthorResponse;
    }

    @Transactional
    public UserAuthorResponse updateAvatar(
            MultipartFile file,
            User user
    ) {

        String fileName =
                filesService.storeFile(file);

        String avatarUrl =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/files/uploads/")
                        .path(fileName)
                        .toUriString();

        user.setAvatarUrl(avatarUrl);

        user = userRepository.save(user);

        return new UserAuthorResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getUserRole()
        );
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!matches) {
            throw new RuntimeException(
                    "Senha inválida"
            );
        }

        String token =
                jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
