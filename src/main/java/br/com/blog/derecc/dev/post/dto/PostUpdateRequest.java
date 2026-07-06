package br.com.blog.derecc.dev.post.dto;

import br.com.blog.derecc.dev.post.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUpdateRequest {

    String title;
    String excerpt;
    String content;
    PostStatus status;

}
