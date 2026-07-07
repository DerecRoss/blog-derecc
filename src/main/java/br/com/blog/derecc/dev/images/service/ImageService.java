package br.com.blog.derecc.dev.images.service;

import br.com.blog.derecc.dev.images.dto.UploadImageResponse;
import br.com.blog.derecc.dev.images.model.ImagesModel;
import br.com.blog.derecc.dev.images.repository.ImageRepository;
import br.com.blog.derecc.dev.post.model.Post;
import br.com.blog.derecc.dev.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FilesService filesService;

    public UploadImageResponse upload(
            Long postId,
            MultipartFile file
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post não encontrado"));

        String storedFileName =
                filesService.storeFile(file);

        ImagesModel image = new ImagesModel();

        image.setOriginalFileName(
                file.getOriginalFilename());

        image.setStoredFileName(
                storedFileName);

        image.setContentType(
                file.getContentType());

        image.setSize(
                file.getSize());

        image.setPost(post);

        imageRepository.save(image);

        String imageUrl =
                "/api/images/" + storedFileName;

        return new UploadImageResponse(
                image.getId(),
                imageUrl
        );
    }
}
