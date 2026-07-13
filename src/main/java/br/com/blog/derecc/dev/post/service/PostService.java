package br.com.blog.derecc.dev.post.service;

import br.com.blog.derecc.dev.post.dto.PostCreateRequest;
import br.com.blog.derecc.dev.post.dto.PostResponse;
import br.com.blog.derecc.dev.post.dto.PostUpdateRequest;
import br.com.blog.derecc.dev.post.enums.PostStatus;
import br.com.blog.derecc.dev.post.model.Post;
import br.com.blog.derecc.dev.post.repository.PostRepository;
import br.com.blog.derecc.dev.user.dto.UserAuthorResponse;
import br.com.blog.derecc.dev.util.slug.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static br.com.blog.derecc.dev.util.mapper.ObjectMapper.parseListObjects;
import static br.com.blog.derecc.dev.util.mapper.ObjectMapper.parseObject;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

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

        var entity = parseObject(postCreateRequest, Post.class);

        entity.setSlug(
                SlugUtils.generate(entity.getTitle())
        );
        entity.setStatus(PostStatus.PUBLISHED);

        entity = postRepository.save(entity);
        logger.info("Saving post in database.");

        PostResponse postResponse = parseObject(entity, PostResponse.class);
        if (entity.getAuthor() != null) {

            UserAuthorResponse userAuthorResponse =
                    parseObject(
                            entity.getAuthor(),
                            UserAuthorResponse.class
                    );

            postResponse.setAuthor(userAuthorResponse);
        }
        return postResponse;
    }

    public PostResponse update(Long id, PostUpdateRequest postUpdateRequest){
        if (postUpdateRequest == null) throw new RuntimeException();

        var entity = postRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        entity.setContent(postUpdateRequest.getContent());
        entity.setExcerpt(postUpdateRequest.getExcerpt());
        entity.setStatus(postUpdateRequest.getStatus());
        entity.setTitle(postUpdateRequest.getTitle());

        entity.setSlug(
                SlugUtils.generate(
                        postUpdateRequest.getTitle()
                )
        );

        entity = postRepository.save(entity);
        logger.info("Update post in database.");

        return parseObject(entity, PostResponse.class);
    }

    public void delete(Long id){
        var entity = postRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        logger.info("Delete post in database.");

        postRepository.delete(entity);
    }
}
