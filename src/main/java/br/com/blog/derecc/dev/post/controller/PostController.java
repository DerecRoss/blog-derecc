package br.com.blog.derecc.dev.post.controller;

import br.com.blog.derecc.dev.post.dto.PostCreateRequest;
import br.com.blog.derecc.dev.post.dto.PostResponse;
import br.com.blog.derecc.dev.post.dto.PostUpdateRequest;
import br.com.blog.derecc.dev.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    public PostService service;

    @GetMapping("/id/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id){
        PostResponse entity = service.findById(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PostResponse>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostResponse> findBySlug(@PathVariable String slug){
        PostResponse entity = service.findBySlug(slug);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>>
    search(
            @RequestParam String q
    ) {
        return ResponseEntity.ok(
                service.search(q)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Page<PostResponse>> findMyPosts(Pageable pageable){
        return ResponseEntity.ok(service.findMyPosts(pageable));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>>
    published(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                service.findPublished(
                        page,
                        size
                )
        );
    }

    @PostMapping
    public ResponseEntity<PostResponse> save(@RequestBody PostCreateRequest postCreateRequest){
        PostResponse entity = service.save(postCreateRequest);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody PostUpdateRequest postUpdateRequest){
        PostResponse entity = service.update(id, postUpdateRequest);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
