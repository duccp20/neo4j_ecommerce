package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.product.ProductPopular;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandRepository brandRepository;
    private final CategoryMapper categoryMapper;

    public Product toEntity(ProductRequest request) {

        return Product.builder()
                .originalPrice(request.getOriginalPrice())
                .sellingPrice(request.getSellingPrice())
                .discountedPrice(request.getDiscountedPrice())
                .quantityAvailable(request.getQuantityAvailable() > 0 ? request.getQuantityAvailable() : null)
                .sellingType(request.getSellingType())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .primaryImage(request.getPrimaryImage())
                .avgRating(request.getRating())
                .brand(request.getBrandName() != null ? brandRepository.findByName(request.getBrandName()) : null)
                .sku(request.getSKU())
                .name(request.getName() != null ? request.getName().trim() : null)
                .reviewOptions(request.getReviewOptions() != null ? request.getReviewOptions() : null)
                .primaryVariantType(request.getPrimaryVariantType())
                .sumSoldQuantity(request.getSoldQuantity() > 0 ? request.getSoldQuantity() : 0)
                .build();
    }


    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId() != null ? product.getId() : null);
        response.setOriginalPrice(product.getOriginalPrice() != null ? product.getOriginalPrice() : null);
        response.setSellingPrice(product.getSellingPrice() != null ? product.getSellingPrice() : null);
        response.setDiscountedPrice(product.getDiscountedPrice() != null ? product.getDiscountedPrice() : null);
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setSellingType(product.getSellingType() != null ? product.getSellingType() : null);
        response.setDescription(product.getDescription() != null ? product.getDescription() : null);
        response.setAvgRating(product.getAvgRating() != null ? product.getAvgRating() : null);
        response.setSumSoldQuantity(product.getSumSoldQuantity() > 0 ? product.getSumSoldQuantity() : 0);
        response.setBrandName(product.getBrand() != null ? product.getBrand().getName() : null);
        response.setSKU(product.getSku());
        response.setName(product.getName());
        response.setPrimaryImage(product.getPrimaryImage());
        response.setCountOfReviews(product.getCountOfReviews());
        response.setSumSoldQuantity(product.getSumSoldQuantity());

        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            response.setImages(product.getProductImages());
        }

        if (product.getProductDimension() != null) {
            response.setProductDimension(product.getProductDimension());
            response.setHasDimensions(true);
        } else {
            response.setHasDimensions(false);
        }

        if (product.getCreatedAt() != null) {
            response.setCreatedAt(product.getCreatedAt());
        }

        if (product.getUpdatedAt() != null) {
            response.setUpdatedAt(product.getUpdatedAt());
        }

        if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
            response.setProductVariants(product.getProductVariants());
            response.setHasVariants(true);
        } else {
            response.setHasVariants(false);
        }

        List<Object> categoriesResponse = new ArrayList<>();
        if (product.getCategories() != null && !product.getCategories().isEmpty()) {
            for (Category category : product.getCategories()) {
                categoriesResponse.add(Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .isFeatured(category.getIsFeatured())
                        .build());
            }
        }
        response.setCategories(categoriesResponse);

        if (product.getProductSpecifications() != null && !product.getProductSpecifications().isEmpty()) {
            response.setProductSpecifications(product.getProductSpecifications());
            response.setHasSpecification(true);
        } else {
            response.setHasSpecification(false);
        }


        if (product.getReviewOptions() != null && !product.getReviewOptions().isEmpty()) {
            response.setReviewOptions(product.getReviewOptions());
        }

        if (product.getPrimaryVariantType() != null) {
            response.setPrimaryVariantType(product.getPrimaryVariantType());
        }

        return response;
    }


    public void updateProduct(Product product, ProductRequest request) {


        product.setOriginalPrice(request.getOriginalPrice() != null ? request.getOriginalPrice() : null);
        product.setSellingPrice(request.getSellingPrice() != null ? request.getSellingPrice() : null);
        product.setDiscountedPrice(request.getDiscountedPrice() != null ? request.getDiscountedPrice() : null);

        if (request.getQuantityAvailable() > 0) {
            product.setQuantityAvailable(request.getQuantityAvailable());
        }
        product.setSellingType(request.getSellingType() != null ? request.getSellingType() : null);
        product.setDescription(request.getDescription() != null ? request.getDescription() : null);
        product.setPrimaryImage(request.getPrimaryImage() != null ? request.getPrimaryImage() : null);
//        product.setRating(request.getRating() != null ? request.getRating() : null);
//        product.setBrandName(request.getBrandName() != null ? request.getBrandName() : null);
        product.setSku(request.getSKU() != null ? request.getSKU() : null);
        product.setName(request.getName() != null ? request.getName() : null);

        if (request.getProductDimension() != null) {
            product.setProductDimension(request.getProductDimension());
        }

    }

    public ProductPopular toPopular(Product product) {

        return ProductPopular.builder()
                .id(product.getId() != null ? product.getId() : null)
                .name(product.getName() != null ? product.getName() : null)
                .image(product.getPrimaryImage() != null ? product.getPrimaryImage() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .avgRating(product.getAvgRating() != null ? product.getAvgRating() : null)
                .sumSoldQuantity(product.getSumSoldQuantity() > 0 ? product.getSumSoldQuantity() : 0)
                .discountedPrice(product.getDiscountedPrice() != null ? product.getDiscountedPrice() : null)
                .sellingPrice(product.getSellingPrice() != null ? product.getSellingPrice() : null)
                .build();
    }
}
