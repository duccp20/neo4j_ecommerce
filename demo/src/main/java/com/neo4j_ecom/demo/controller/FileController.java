package com.neo4j_ecom.demo.controller;

import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.service.FileService;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<String>>> uploadFile(
            @RequestParam("folder") String folder,
            @RequestParam("file") MultipartFile[] files
    ) throws URISyntaxException, IOException, InterruptedException {
        List<File> fileList = Arrays.stream(files)
                .map(file -> {
                    try {
                        File localFile = File.createTempFile("image_", file.getOriginalFilename());
                        file.transferTo(localFile);
                        return localFile;
                    } catch (IOException e) {
                        throw new RuntimeException("Error saving file", e);
                    }
                })
                .collect(Collectors.toList());
        List<String> fileURLs = fileService.storeFileS3(fileList,folder);
        SuccessCode successCode = SuccessCode.UPLOADED;
        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(fileURLs)
                .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<List<String>>> deleteFile(
           @RequestBody List<String> imageURLs
    ) throws FileNotFoundException {
        fileService.deleteFileS3(imageURLs);
        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                        .statusCode(SuccessCode.DELETED.getCode())
                        .message(SuccessCode.DELETED.getMessage())
                .build());
    }


}
