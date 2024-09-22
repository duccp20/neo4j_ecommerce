package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ProductBannerService {

    ProductBannerResponse handleCreateBanner(ProductBannerRequest request);

    ProductBannerResponse handleGetBannerById(String bannerId);

    ProductBannerResponse handleUpdateBanner(String bannerId, ProductBannerRequest request);

    Void handleDeleteBannerById(String bannerId);

    List<ProductBannerResponse> handleGetBannersByQuantity(int quantity);

    List<String> handleGetBannerImagesByQuantity(int quantity);

    List<ProductBannerResponse> handleGetBanners();
}
