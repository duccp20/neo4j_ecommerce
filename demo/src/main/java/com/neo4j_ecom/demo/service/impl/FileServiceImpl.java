package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.service.FileService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.image.base-uri}")
    private String baseURI;


    @Override
    public void createUploadedFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                log.info("created directory {}", tmpDir.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("directory {} already exists", tmpDir.getAbsolutePath());
        }
    }

    @Override
    public String storeFile(MultipartFile file, String folder) throws URISyntaxException {
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        log.info("finalName in storeFile: {}", finalName);

        finalName = finalName.replaceAll("\\s", "");

        URI uri = new URI(baseURI + folder + "/" + finalName);


        log.info("uri in storeFile: {}", uri);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return finalName;
    }

    @Override
    public void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_REQUIRE_PARAM);
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "png");
        List<String> allowedMimeTypes = Arrays.asList(
                "image/jpg",
                "image/jpeg",
                "image/png"
        );

        // Validate extension
        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext ->
                fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new AppException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        // Validate MIME type
        String contentType = file.getContentType();
        log.info("contentType: {}", contentType);

        boolean isValidMimeType = allowedMimeTypes.contains(contentType);
        log.info("isValidMimeType: {}", isValidMimeType);

        if (!isValidMimeType) {
            throw new AppException(ErrorCode.INVALID_FILE_MIME_TYPE);
        }

        // Check file size
        long maxSize = 1024 * 1024 * 10; // 10 MB in bytes

        log.info("file size: {}", file.getSize());
        if (file.getSize() > maxSize) {
            throw new AppException(ErrorCode.INVALID_FILE_SIZE);
        }
    }

}
