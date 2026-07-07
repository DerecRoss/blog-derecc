package br.com.blog.derecc.dev.images.controller;

import br.com.blog.derecc.dev.images.dto.UploadImageResponse;
import br.com.blog.derecc.dev.images.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/{postId}/images")
    public ResponseEntity<UploadImageResponse> upload(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity.ok(
                imageService.upload(postId, file)
        );
    }
}
