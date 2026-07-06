package br.com.blog.derecc.dev.images;

import br.com.blog.derecc.dev.images.dto.UploadFileResponseDto;
import br.com.blog.derecc.dev.images.service.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class FileController {

    @Autowired
    private FilesService service;

    @PostMapping("/uploadFileName")
    public UploadFileResponseDto uploadImage(@RequestParam("file") MultipartFile file){
        var fileName = service.storeFile(file);
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDto(
                fileName,
                fileDownloadUri,
                file.getContentType(),
                file.getSize()
        );
    }

    @GetMapping("/downloadFile/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename, HttpServletRequest request){
        Resource resource = service.loadFile(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (contentType == null) contentType = "aplication/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, // attach in header of response.
                        "attachment; filename=\""
                                + resource.getFilename() + "\"")
                .body(resource);
    }
}
