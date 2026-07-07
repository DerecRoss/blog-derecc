package br.com.blog.derecc.dev.post.repository;

import br.com.blog.derecc.dev.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
