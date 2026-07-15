package br.com.blog.derecc.dev.images.service;

import br.com.blog.derecc.dev.images.dto.UploadImageResponse;
import br.com.blog.derecc.dev.images.model.ImagesModel;
import br.com.blog.derecc.dev.images.repository.ImageRepository;
import br.com.blog.derecc.dev.post.model.Post;
import br.com.blog.derecc.dev.post.repository.PostRepository;
import br.com.blog.derecc.dev.security.service.AuthService;
import br.com.blog.derecc.dev.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    private AuthService authService;

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FilesService filesService;

    private static final List<String> ALLOWED_TYPES =
            List.of(
                    "image/png",
                    "image/jpeg",
                    "image/webp"
            );

    public UploadImageResponse upload(
            Long postId,
            MultipartFile file
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post não encontrado"));

        validateOwnership(post);

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

        String contentType = file.getContentType();

        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new RuntimeException(
                    "Content type is not allowed."
            );
        }

        imageRepository.save(image);

        String imageUrl =
                "/api/files/uploads/" + storedFileName;

        return new UploadImageResponse(
                image.getId(),
                imageUrl
        );
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
}
