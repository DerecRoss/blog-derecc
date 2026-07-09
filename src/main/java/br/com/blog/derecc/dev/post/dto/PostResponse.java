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

    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<String> imageUrls;
}
