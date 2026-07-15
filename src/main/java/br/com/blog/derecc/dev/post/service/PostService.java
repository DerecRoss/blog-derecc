package br.com.blog.derecc.dev.post.service;

import br.com.blog.derecc.dev.post.dto.PostCreateRequest;
import br.com.blog.derecc.dev.post.dto.PostResponse;
import br.com.blog.derecc.dev.post.dto.PostUpdateRequest;
import br.com.blog.derecc.dev.post.enums.PostStatus;
import br.com.blog.derecc.dev.post.model.Post;
import br.com.blog.derecc.dev.post.repository.PostRepository;
import br.com.blog.derecc.dev.security.service.AuthService;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.user.model.User;
import br.com.blog.derecc.dev.util.slug.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static br.com.blog.derecc.dev.util.mapper.ObjectMapper.parseListObjects;
import static br.com.blog.derecc.dev.util.mapper.ObjectMapper.parseObject;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;



    private final Logger logger = Logger.getLogger(PostService.class.getName());

    public PostResponse findById(Long id){
        var entity = postRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        logger.info("Search post in database.");

        return parseObject(entity, PostResponse.class);
    }

    public Page<PostResponse> findAll(Pageable pageable){
        var posts = postRepository.findAll(pageable);

        logger.info("Listing all posts in database.");

        return posts.map(p -> {
            return parseObject(p, PostResponse.class);
        });
    }

    public List<PostResponse> search(
            String title
    ) {
        List<Post> posts =
                postRepository
                        .findByTitleContainingIgnoreCase(
                                title
                        );
        return parseListObjects(
                posts,
                PostResponse.class
        );
    }

    public PostResponse findBySlug(String slug) {
        var entity = postRepository.findBySlug(slug).orElseThrow(RuntimeException::new);

        return parseObject(entity, PostResponse.class);
    }

    public Page<PostResponse> findPublished(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        Page<Post> posts =
                postRepository.findByStatus(
                        PostStatus.PUBLISHED,
                        pageable
                );

        return posts.map(
                post ->
                        parseObject(
                                post,
                                PostResponse.class
                        )
        );
    }

    public PostResponse save(PostCreateRequest postCreateRequest){
        if (postCreateRequest == null) throw new RuntimeException();

        Post entity = parseObject(postCreateRequest, Post.class);

        User authenticatedUser = authService.getAuthenticatedUser();

        entity.setAuthor(authenticatedUser);

        entity = postRepository.save(entity);
        logger.info("Saving post in database.");

        PostResponse postResponse = parseObject(entity, PostResponse.class);
        if (entity.getAuthor() != null) {

            UserAuthorResponse author =
                    parseObject(
                            entity.getAuthor(),
                            UserAuthorResponse.class
                    );

            postResponse.setAuthor(author);
        }
        return postResponse;
    }

    public PostResponse update(Long id, PostUpdateRequest postUpdateRequest){
        if (postUpdateRequest == null) throw new RuntimeException();

        var post = postRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        User user =
                authService.getAuthenticatedUser();

        if (!post.getAuthor()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "User cant edit this."
            );
        }

        post.setContent(postUpdateRequest.getContent());
        post.setExcerpt(postUpdateRequest.getExcerpt());
        post.setStatus(postUpdateRequest.getStatus());
        post.setTitle(postUpdateRequest.getTitle());

        post.setSlug(
                SlugUtils.generate(
                        postUpdateRequest.getTitle()
                )
        );

        post = postRepository.save(post);
        logger.info("Update post in database.");

        return parseObject(post, PostResponse.class);
    }

    public Page<PostResponse> findMyPosts(Pageable pageable){

        User user = authService.getAuthenticatedUser();

        Page<Post> posts =
                postRepository.findByAuthor(
                        user,
                        pageable
                );

        return posts.map(this::toResponse);
    }

    private void validateOwnership(Post post){

        User currentUser =
                authService.getAuthenticatedUser();

        if (!post.getAuthor()
                .getId()
                .equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "User cant edit this."
            );
        }
    }

    private PostResponse toResponse(Post post) {

        PostResponse response = new PostResponse();

        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setSlug(post.getSlug());
        response.setExcerpt(post.getExcerpt());
        response.setContent(post.getContent());
        response.setStatus(post.getStatus());
        response.setCreatedAt(post.getCreatedAt());

        if (post.getAuthor() != null) {

            UserAuthorResponse author = new UserAuthorResponse();

            author.setId(post.getAuthor().getId());
            author.setUsername(post.getAuthor().getUsername());
            author.setAvatarUrl(post.getAuthor().getAvatarUrl());

            response.setAuthor(author);
        }

        return response;
    }

    public void delete(Long id){
        var entity = postRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        logger.info("Delete post in database.");

        validateOwnership(entity);

        postRepository.delete(entity);
    }
}
