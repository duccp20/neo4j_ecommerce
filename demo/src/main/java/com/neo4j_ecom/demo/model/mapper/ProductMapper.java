package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                .SKU(request.getSKU())
                .name(request.getName() != null ? request.getName().trim() : null)
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
        response.setRating(product.getAvgRating() != null ? product.getAvgRating() : null);
        response.setSumSoldQuantity(product.getSumSoldQuantity() > 0 ? product.getSumSoldQuantity() : 0);
        response.setBrandName(product.getBrand() != null ? product.getBrand().getName() : null);
        response.setSKU(product.getSKU());
        response.setName(product.getName());
        response.setPrimaryImage(product.getPrimaryImage());

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
        product.setSKU(request.getSKU() != null ? request.getSKU() : null);
        product.setName(request.getName() != null ? request.getName() : null);

        if (request.getProductDimension() != null) {
            product.setProductDimension(request.getProductDimension());
        }

    }
}
