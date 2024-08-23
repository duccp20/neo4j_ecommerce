package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.ProductImageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;

    @Override
    public String createImage(MultipartFile file) {
        return null;
    }

    @Override
    public String updateFileImage(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteFileImage(String Imageurl) {


    }
}
