package br.com.blog.derecc.dev.images.repository;

import br.com.blog.derecc.dev.images.model.ImagesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImagesModel, Long> {
}
