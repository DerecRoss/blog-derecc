package br.com.blog.derecc.dev.post.repository;

import br.com.blog.derecc.dev.post.enums.PostStatus;
import br.com.blog.derecc.dev.post.model.Post;
import br.com.blog.derecc.dev.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByTitleContainingIgnoreCase(String title);

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByAuthor(User author, Pageable pageable);
}
