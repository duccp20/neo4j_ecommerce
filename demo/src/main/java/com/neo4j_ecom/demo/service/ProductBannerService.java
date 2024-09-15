package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ProductBannerService {

    ProductBannerResponse handleCreateBanner(ProductBannerRequest request, String productId, List<MultipartFile> files) throws URISyntaxException, IOException;

    ProductBannerResponse handleGetBannerById(String bannerId);

    List<ProductBannerResponse> handleGetBannersByProductId(String productId);

    ProductBannerResponse handleUpdateBanner(String productId, String bannerId, ProductBannerRequest request, List<MultipartFile> files) throws URISyntaxException, IOException;

    Void handleDeleteBannerByProductId(String productId);

    Void handleDeleteBannerById(String bannerId);

    Void handleDeleteBannerImage(String bannerId, String imgUrl) throws FileNotFoundException;
    ProductBannerResponse handleUpdateBannerFiles(String bannerId, List<MultipartFile> files) throws URISyntaxException, IOException;

    ProductBannerResponse handleUpdateBannerPrimary(String bannerId, String url);

    List<ProductBannerResponse> handleGetBannersByQuantity(int quantity);


    List<String> handleGetBannerImagesByQuantity(int quantity);
}
