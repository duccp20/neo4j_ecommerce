package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.request.ProductVariantRequest;
import com.neo4j_ecom.demo.model.dto.response.product.ProductPopular;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Category;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandRepository brandRepository;
    private final CategoryMapper categoryMapper;

    public Product toEntity(ProductRequest request) {

        return Product.builder()
                .originalPrice(request.getOriginalPrice() == null ? null : request.getOriginalPrice())
                .sellingPrice(request.getSellingPrice() == null ? null : request.getSellingPrice())
                .discountedPrice(request.getDiscountedPrice() == null ? null : request.getDiscountedPrice())
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
                .soldQuantity(request.getSoldQuantity())
                .sumSoldQuantity(request.getSoldQuantity() + (request.getProductVariants() == null ? 0L : request.getProductVariants().stream().mapToLong(ProductVariantRequest::getSoldQuantity).sum()))
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
        response.setSumSoldQuantity(product.getSoldQuantity() + (product.getProductVariants() == null ? 0 : product.getProductVariants().stream().mapToLong(ProductVariant::getSoldQuantity).sum()));

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
        List<ProductVariant> variants = product.getProductVariants() != null ? product.getProductVariants() : new ArrayList<>();
        List<BigDecimal> sellingPrices = variants.stream().map(variant -> variant.getSellingPrice()).filter(Objects::nonNull).collect(Collectors.toList());

       if (product.getSellingPrice() != null) {
           sellingPrices.add(product.getSellingPrice());
       }
        List<BigDecimal> discountedPrices = variants.stream().map(variant -> variant.getDiscountedPrice()).filter(Objects::nonNull).collect(Collectors.toList());

       if (product.getDiscountedPrice() != null) {
           discountedPrices.add(product.getDiscountedPrice());
       }

        BigDecimal minSellingPrice = sellingPrices.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxSellingPrice = sellingPrices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal minDiscountedPrice = discountedPrices.stream().filter(p -> p.compareTo(BigDecimal.ZERO) > 0).min(BigDecimal::compareTo).orElse(null);
        BigDecimal maxDiscountedPrice = discountedPrices.stream().filter(p -> p.compareTo(BigDecimal.ZERO) > 0).max(BigDecimal::compareTo).orElse(null);
        return ProductPopular.builder()
                .id(product.getId() != null ? product.getId() : null)
                .name(product.getName() != null ? product.getName() : null)
                .image(product.getPrimaryImage() != null ? product.getPrimaryImage() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .avgRating(product.getAvgRating() != null ? product.getAvgRating() : 0)
                .sumSoldQuantity(product.getSumSoldQuantity())
                .minSellingPrice(minSellingPrice)
                .maxSellingPrice(maxSellingPrice)
                .minDiscountedPrice(minDiscountedPrice)
                .maxDiscountedPrice(maxDiscountedPrice)
                .build();
    }
}
