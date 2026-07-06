package br.com.blog.derecc.dev.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCreateRequest {

    private String title;
    private String content;
    private String excerpt;

}
