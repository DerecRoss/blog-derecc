package br.com.blog.derecc.dev.images.service;

import br.com.blog.derecc.dev.config.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FilesService {

    private Path fileStorageLocation;

    @Autowired
    public FilesService(FileStorageConfig fileStorageConfig){
        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath()
                .toAbsolutePath()
                .normalize();

        this.fileStorageLocation = path;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public String storeFile(MultipartFile file){

        String originalName = file.getOriginalFilename();

        String extension =
                originalName.substring(
                        originalName.lastIndexOf(".")
                );

        String fileName =
                UUID.randomUUID() + extension;


//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try{
            if (fileName.contains("../")) throw new RuntimeException();

            Path target = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadFile(String fileName){
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()){
                return resource;
            }else {
                throw new RuntimeException();
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
