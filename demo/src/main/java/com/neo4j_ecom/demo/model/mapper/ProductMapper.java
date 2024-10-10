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
                .sku(request.getSku())
                .name(request.getName() != null ? request.getName().trim() : null)
                .primaryVariantType(request.getPrimaryVariantType())
                .soldQuantity(request.getSoldQuantity())
                .sumSoldQuantity(request.getSoldQuantity() + (request.getProductVariants() == null ? 0L : request.getProductVariants().stream().mapToLong(ProductVariantRequest::getSoldQuantity).sum()))
                .build();
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
