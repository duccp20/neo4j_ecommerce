package com.neo4j_ecom.demo.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {

    void createUploadedFolder(String folder) throws URISyntaxException;

    String storeFile(MultipartFile file, String folder) throws URISyntaxException;

    void validateFile(MultipartFile file);

    String storeFileFirebase(MultipartFile file, String folder) throws URISyntaxException, IOException;

    void deleteFileFirebase(String path) throws FileNotFoundException;


    String storeFileS3(MultipartFile file, String folder) throws URISyntaxException, IOException, InterruptedException;

}
