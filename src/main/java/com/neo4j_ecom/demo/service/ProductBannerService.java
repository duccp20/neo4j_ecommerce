package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.entity.ProductBanner;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ProductBannerService {

    ProductBanner handleCreateBanner(ProductBanner request);

    ProductBanner handleGetBannerById(String bannerId);

    ProductBanner handleUpdateBanner(String bannerId, ProductBanner request);

    Void handleDeleteBannerById(String bannerId);

    List<ProductBanner> handleGetBannersByQuantity(int quantity);

    List<String> handleGetBannerImagesByQuantity(int quantity);

    List<ProductBanner> handleGetBanners();
}
