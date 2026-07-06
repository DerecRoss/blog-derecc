package br.com.blog.derecc.dev.post.model;

import br.com.blog.derecc.dev.post.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    private String excerpt;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
