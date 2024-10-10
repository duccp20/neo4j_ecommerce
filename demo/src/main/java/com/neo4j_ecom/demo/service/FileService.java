package com.neo4j_ecom.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FileService {

    void createUploadedFolder(String folder) throws URISyntaxException;

    String storeFile(MultipartFile file, String folder) throws URISyntaxException;

    void validateFile(MultipartFile file);

    String storeFileFirebase(MultipartFile file, String folder) throws URISyntaxException, IOException;

    void deleteFileFirebase(String path) throws FileNotFoundException;

    List<String> storeFileS3(List<File> files, String folder) throws URISyntaxException, IOException, InterruptedException;

    void deleteFileS3(List<String> imageURLs) throws FileNotFoundException;
}
