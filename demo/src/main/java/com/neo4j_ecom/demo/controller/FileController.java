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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @RequestParam("folder") String folder,
            @RequestParam("file") MultipartFile[] files
    ) throws URISyntaxException, IOException, InterruptedException {
        List<File> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            File localFile = File.createTempFile("image_", file.getOriginalFilename());
            file.transferTo(localFile);
            fileList.add(localFile);
        }
        List<String> fileURLs = fileService.storeFileS3(fileList,folder);
        SuccessCode successCode = SuccessCode.UPLOADED;
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .statusCode(successCode.getCode())
                        .message(successCode.getMessage())
                        .data(fileURLs.toString())
                .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteFile(
            @RequestParam("path") String path
    ) throws FileNotFoundException {
        fileService.deleteFileFirebase(path);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .statusCode(SuccessCode.DELETED.getCode())
                        .message(SuccessCode.DELETED.getMessage())
                .build());
    }


}
