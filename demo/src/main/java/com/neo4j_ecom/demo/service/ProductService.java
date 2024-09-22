package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ProductService {
    ProductResponse handleCreateProduct(ProductRequest request, List<MultipartFile> files) throws URISyntaxException, IOException;

    String handleCreateProductImage(MultipartFile file) throws URISyntaxException;

    ProductResponse handleGetProductById(String id);

    List<ProductResponse> handleGetAllProducts();

    ProductResponse handleUpdateProduct(String id, ProductRequest request);

    Void handleDeleteProduct(String id);

    List<String> HandleCreateProductImages(String productId, List<MultipartFile> files) throws URISyntaxException, IOException;

    Void handleDeleteProductImage(String id, String imgUrl);

    Void handleSetPrimaryImage(String productId, String imgUrl);

    PaginationResponse handleGetProductPopularBySoldQuantity( int page, int size);

    Boolean handleProductExists(String name);
}
