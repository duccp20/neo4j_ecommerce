package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.request.ProductVariantRequest;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductPopular;
import com.neo4j_ecom.demo.model.entity.*;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.model.mapper.ProductVariantMapper;
import com.neo4j_ecom.demo.repository.*;
import com.neo4j_ecom.demo.service.*;

import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductVariantRepository productVariantRepository;
    private final ProductDimensionService productDimensionService;
    private final ProductVariantMapper variantMapper;

    @Override
    public Product createProduct(ProductRequest request) {


        //if it has variant then original price and selling price is required
        if (!request.getHasVariants() && (request.getOriginalPrice() == null || request.getSellingPrice() == null)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_REQUIRED_PRICE);
        }

        //validate price
        if (!request.getHasVariants()) {
            this.validateProductPrices(request);
        } else {
            request.setOriginalPrice(null);
            request.setSellingPrice(null);
            request.setDiscountedPrice(null);
        }

        Product product = productMapper.toEntity(request);

        product.setDescription(
                Optional.ofNullable(request.getDescription())
                        .filter(description -> !description.isEmpty())
                        .map(description -> description.replaceAll("(?s)<script[^>]*>.*?<\\/script>", ""))
                        .map(description -> description.replaceAll("(SELECT|INSERT|UPDATE|DELETE).*", ""))
                        .orElse(null)
        );

        //variant
        if (request.getHasVariants() && request.getProductVariants() != null) {

            List<ProductVariant> productVariants = new ArrayList<>();
            for (ProductVariantRequest productVariantRequest : request.getProductVariants()) {

                if (productVariantRequest.getOriginalPrice() == null || productVariantRequest.getSellingPrice() == null) {
                    throw new AppException(ErrorCode.PRODUCT_NOT_REQUIRED_PRICE);
                }

                this.validateProductVariantPrices(productVariantRequest);
                ProductVariant productVariant = variantMapper.toEntity(productVariantRequest);
                productVariants.add(productVariantRepository.save(productVariant));

            }
            product.setProductVariants(productVariants);
        } else {
            product.setProductVariants(null);
        }

        //dimension
        if (request.getProductDimension() != null) {
            if (request.getProductDimension().getWidth() == null
                    && request.getProductDimension().getLength() == null
                    && request.getProductDimension().getWeight() == null
                    && request.getProductDimension().getBreadth() == null
            ) {
                product.setProductDimension(null);
            } else {
                product.setProductDimension(ProductDimension.builder()
                        .breadth(request.getProductDimension().getBreadth())
                        .length(request.getProductDimension().getLength())
                        .weight(request.getProductDimension().getWeight())
                        .width(request.getProductDimension().getWidth())
                        .packageUnit(request.getProductDimension().getPackageUnit())
                        .unitWeight(request.getProductDimension().getUnitWeight())
                        .build());
            }
        } else {
            product.setProductDimension(null);
        }

        //images
        product.setProductImages(request.getProductImages());
        if (!product.getProductImages().isEmpty()) {
            product.setPrimaryImage(product.getProductImages().get(0));
        } else {
            product.setPrimaryImage(null);
        }

        //categories and collections
        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            if (categories.size() != request.getCategoryIds().size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            product.setCategories(categories);
            product.setHasCollection(categories.stream().anyMatch(Category::getIsFeatured));
        }

        //specification
        if (request.getHasSpecification() && request.getSpecifications() != null) {
            product.setProductSpecifications(request.getSpecifications());
        }

        //brand
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }

        return productRepository.save(product);
    }

    @Override
    public Map<String, Object> getProductById(String id) {


        Product product = this.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus().equals(Status.DELETED)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        //options
        List<ProductVariant> variants = product.getProductVariants();
        Map<ProductType, Set<String>> options = new HashMap<>();
        for (ProductVariant variant : variants) {
            for (VariantOption option : variant.getVariantOptions()) {
                options.computeIfAbsent(option.getProductType(), k -> new HashSet<>())
                        .add(option.getValueName());
            }
        }
        return Map.of("product", product, "options", options);

    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(String id, ProductRequest request) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("product: {}", product);

        product.updateProduct(request);


        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            String description = request.getDescription()
                    .replaceAll("(?s)<script[^>]*>.*?<\\/script>", "")
                    .replaceAll("(SELECT|INSERT|UPDATE|DELETE).*", "");
            product.setDescription(description);
        }

        if (request.getProductDimension() != null) {
            product.setProductDimension(productDimensionService.
                    updateProductDimension(product.getProductDimension(), request.getProductDimension()));
        }

        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            if (categories.size() != request.getCategoryIds().size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            product.setCategories(categories);
            product.setHasCollection(categories.stream().anyMatch(Category::getIsFeatured));
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }

        if (request.getHasVariants() && request.getProductVariants() != null) {
            product.setProductVariants(request.getProductVariants().stream().map(variantMapper::toEntity).collect(Collectors.toList()));
        }

        return productRepository.save(product);

    }

    @Override
    public Void deleteProduct(String id) {

        Product product = this.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus().equals(Status.DELETED)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        product.setStatus(Status.DELETED);
        productRepository.save(product);

        return null;
    }

    @Override
    public PaginationResponse getTopProductsSold(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "sumSoldQuantity");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductPopular> products = productPage.getContent().stream()
                .filter(product -> !product.getStatus().equals(Status.DELETED))
                .map(productMapper::toPopular)
                .collect(Collectors.toList());

        Meta meta = Meta.builder()
                .current(productPage.getNumber() + 1)
                .pageSize(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .isFirstPage(productPage.isFirst())
                .isLastPage(productPage.isLast())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .result(products)
                .build();
    }

    @Override
    public Boolean productExists(String name) {
        return productRepository.existsByName(name);
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    private void validateProductPrices(ProductRequest request) {
        if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0 ||
                request.getOriginalPrice().compareTo(request.getDiscountedPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }


    private void validateProductVariantPrices(ProductVariantRequest request) {

        if (request.getOriginalPrice() == null || request.getSellingPrice() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_REQUIRED_PRICE);
        }

        if (request.getDiscountedPrice() != null &&
                (request.getOriginalPrice().compareTo(request.getDiscountedPrice()) >= 0 ||
                        request.getOriginalPrice().compareTo(request.getSellingPrice()) >= 0 ||
                        request.getDiscountedPrice().compareTo(request.getSellingPrice()) >= 0)) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        } else if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }



}