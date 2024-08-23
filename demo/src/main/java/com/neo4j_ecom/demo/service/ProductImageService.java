package com.neo4j_ecom.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService{

    String createImage(MultipartFile file);

    String  updateFileImage(MultipartFile file);

    void deleteFileImage(String Imageurl);

}
