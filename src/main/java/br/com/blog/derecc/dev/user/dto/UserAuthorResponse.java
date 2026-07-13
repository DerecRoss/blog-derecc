package br.com.blog.derecc.dev.user.dto;

import br.com.blog.derecc.dev.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorResponse {

    private String email;
    private String username;
    private Long id;
    private String avatarUrl;
    private UserRole userRole;

}
