package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {

        Product product = new Product();
        product.setOriginalPrice(request.getOriginalPrice() != null ? request.getOriginalPrice() : null);
        product.setSellingPrice(request.getSellingPrice() != null ? request.getSellingPrice() : null);
        product.setDiscountedPrice(request.getDiscountedPrice() != null ? request.getDiscountedPrice() : null);

        if (request.getQuantityAvailable() > 0) {
            product.setQuantityAvailable(request.getQuantityAvailable());
        }
        product.setSellingType(request.getSellingType() != null ? request.getSellingType() : null);
        product.setDescription(request.getDescription() != null ? request.getDescription() : null);
        product.setPrimaryImage(request.getPrimaryImage() != null ? request.getPrimaryImage() : null);
        product.setRating(request.getRating() != null ? request.getRating() : null);
        product.setBrandName(request.getBrandName() != null ? request.getBrandName() : null);
        product.setSKU(request.getSKU() != null ? request.getSKU() : null);
        product.setName(request.getName() != null ? request.getName() : null);

        return product;
    }


    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setOriginalPrice(product.getOriginalPrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setDiscountedPrice(product.getDiscountedPrice());
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setSellingType(product.getSellingType());
        response.setDescription(product.getDescription());
        response.setRating(product.getRating());
        response.setSumSoldQuantity(product.getSumSoldQuantity());
        response.setBrandName(product.getBrandName());
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

            for (ProductVariant productVariant : product.getProductVariants()) {

                ProductSpecification spec = productVariant.getProductSpecifications();
                productVariant.setHasSpecification(spec != null);

            }

            response.setProductVariants(product.getProductVariants());
            response.setHasVariants(true);

        } else {
            response.setHasVariants(false);
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
        product.setRating(request.getRating() != null ? request.getRating() : null);
        product.setBrandName(request.getBrandName() != null ? request.getBrandName() : null);
        product.setSKU(request.getSKU() != null ? request.getSKU() : null);
        product.setName(request.getName() != null ? request.getName() : null);

        if (request.getProductDimension() != null) {
            product.setProductDimension(request.getProductDimension());
        }

    }
}
