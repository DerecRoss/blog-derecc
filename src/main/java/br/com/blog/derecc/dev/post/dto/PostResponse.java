package br.com.blog.derecc.dev.post.dto;

import br.com.blog.derecc.dev.post.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponse {

    Long id;
    String title;
    String slug;
    String excerpt;
    String content;
    PostStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
