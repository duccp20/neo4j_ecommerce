package com.neo4j_ecom.demo.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface FileService {

    void createUploadedFolder(String folder) throws URISyntaxException;

    String storeFile(MultipartFile file, String folder) throws URISyntaxException;

    void validateFile(MultipartFile file);
}
