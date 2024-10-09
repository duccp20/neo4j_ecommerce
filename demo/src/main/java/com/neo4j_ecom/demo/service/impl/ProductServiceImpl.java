package com.neo4j_ecom.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.request.ProductVariantRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.Meta;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.product.ProductPopular;
import com.neo4j_ecom.demo.model.dto.response.product.ProductResponse;
import com.neo4j_ecom.demo.model.entity.*;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.model.mapper.ProductVariantMapper;
import com.neo4j_ecom.demo.repository.*;
import com.neo4j_ecom.demo.service.*;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.Status;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${file.image.base-uri}")
    private String baseURI;
    @Value("${file.image.folder.product}")
    private String folder;
    private final ProductRepository productRepository;

    private final ReviewOptionRepository reviewOptionRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final ProductImageService productImageService;

    private final ProductMapper productMapper;

    private final CategoryMapper categoryMapper;

    private final FileService fileService;

    private final ProductDimensionRepository productDimensionRepository;

    private final ProductVariantRepository productVariantRepository;

    private final ProductSpecificationRepository productSpecificationRepository;

    private final ProductDimensionService productDimensionService;

    private final CategoryService categoryService;

    private final ProductReviewMapper reviewMapper;

    private final ProductVariantMapper variantMapper;

    private final UserService userService;

    private final AuthService authService;


    //===================== PRODUCT ====================
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
        if (!Collections.emptyList().equals(request.getProductImages())) {
            product.setProductImages(request.getProductImages());
        } else {
            product.setProductImages(null);
        }
        if (request.getProductImages().size() > 0) {
            product.setPrimaryImage(request.getProductImages().get(0));
        }

        //categories and collections
        if (request.getCategoryIds() != null) {
            List<Category> categories = new ArrayList<>();
            for (String id : request.getCategoryIds()) {
                Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                categories.add(category);
            }
            product.setCategories(categories);
        }

        //specification
        if (request.getHasSpecification() && request.getSpecifications() != null) {
            product.setProductSpecifications(request.getSpecifications());
        }

        Product savedProduct = productRepository.save(product);

//        //save product to category/collection
//        List<Category> categories = product.getCategories();
//        if (categories != null && !categories.isEmpty()) {
//            for (Category category : categories) {
//                category.getProducts().add(savedProduct);
//                categoryRepository.save(category);
//            }
//        }

        //save product to brand
        Brand brand = product.getBrand();
        brand.getProducts().add(savedProduct);
        brandRepository.save(brand);


        return savedProduct;
    }

    @Override
    public Map<String, Object> getProductById(String id) {

        Product product = this.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

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
        return  productRepository.findById(id);
    }


    private void validateProductPrices(ProductRequest request) {
        if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0 ||
                request.getOriginalPrice().compareTo(request.getDiscountedPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }

    private void validateProductVariantPrices(ProductVariantRequest request) {
        if (request.getDiscountedPrice() != null &&
                (request.getOriginalPrice().compareTo(request.getDiscountedPrice()) >= 0 ||
                        request.getOriginalPrice().compareTo(request.getSellingPrice()) >= 0 ||
                        request.getDiscountedPrice().compareTo(request.getSellingPrice()) >= 0)) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        } else if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {


        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("product: {}", product);

        boolean existedProduct = productRepository.existsByName(request.getName().trim());

        if (existedProduct && !product.getName().equals(request.getName().trim())) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        productMapper.updateProduct(product, request);

        if (request.getProductDimension() != null) {

            product.setProductDimension(productDimensionService.
                    updateProductDimension(product.getProductDimension(), request.getProductDimension()));

        }

        if (request.getCategoryIds() != null) {
            List<Category> categories = new ArrayList<>();
            for (String identifier : request.getCategoryIds()) {

                log.info("identifier of category in update product: {}", identifier);

                CategoryResponse category = categoryService.handleGetCategoryById(identifier);
//                Category categoryEntity = this.toCategory(category);
//                categories.add(categoryEntity);
            }
            product.setCategories(categories);
        }

        Product savedProductAfterUpdate = productRepository.save(product);

        //        if (product.getCategories() != null) {
//            List<CategoryResponse> categoryResponses = product.getCategories().stream().map(category -> CategoryResponse.builder().id(category.getId()).name(category.getName()).build()).collect(Collectors.toList());
//
//            response.setCategories(categoryResponses);
//        }

        return productMapper.toResponse(savedProductAfterUpdate);

    }

    @Override
    public Void deleteProduct(String id) {


        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!authService.getCurrentUserEmail().equals(product.getUpdatedBy())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        product.setStatus(Status.DELETED);

        productRepository.save(product);
        ;
        return null;
    }

    @Override
    public PaginationResponse getProductPopularBySoldQuantity(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "sumSoldQuantity");
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size, sort));
        List<Product> products = productPage.getContent();

        List<ProductPopular> productPopulars = products.stream()
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
                .result(productPopulars)
                .build();
    }

    @Override
    public Boolean productExists(String name) {

        return productRepository.existsByName(name);
    }




    @Override
    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));

        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }


    //==================PRODUCT IMAGES====================
    private List<String> handleCreateProductImages(List<MultipartFile> files, Product product) throws URISyntaxException, IOException {

        List<String> images = new ArrayList<>();
        if (files != null) {

            for (MultipartFile file : files) {

                //local
                // String fileName = this.handleCreateProductImage(file);

                //firebase
                String url = this.createProductImageFirebase(file);

                images.add(url);
            }
        }

        return images;
    }

    @Override
    public String createProductImage(MultipartFile file) throws URISyntaxException {
        //validate file
        fileService.validateFile(file);

        //create dir if not existed
        fileService.createUploadedFolder(baseURI + folder);

        //store file
        String fileName = fileService.storeFile(file, "products");

        log.info("file name: {}", fileName);

        String urlImage = baseURI + folder + "/" + fileName;

        log.info("url image: {}", urlImage);

        return urlImage;
    }


    private String createProductImageFirebase(MultipartFile file) throws URISyntaxException, IOException {
        //validate file
        fileService.validateFile(file);

        String urlImage = fileService.storeFileFirebase(file, "products/images");

        log.info("url image: {}", urlImage);

        return urlImage;
    }

    @Override
    public List<String> createProductImages(String productId, List<MultipartFile> files) throws URISyntaxException, IOException {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> newProductImage = new ArrayList<>();

        //current product image
        newProductImage.addAll(product.getProductImages());

        //new product image
        List<String> listProductImage = this.handleCreateProductImages(files, product);
        newProductImage.addAll(listProductImage);

        product.setProductImages(newProductImage);
        productRepository.save(product);

        return listProductImage;
    }

    @Override
    public Void deleteProductImage(String id, String imgUrl) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> productImages = product.getProductImages();

        for (String s : productImages) {
            if (s.equals(imgUrl)) {
                productImages.remove(s);
                break;
            }
        }

        product.setProductImages(productImages);
        productRepository.save(product);

        return null;
    }

    @Override
    public Void setPrimaryImage(String productId, String imgUrl) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> productImages = product.getProductImages();

        for (String s : productImages) {
            if (s.equals(imgUrl)) {
                product.setPrimaryImage(imgUrl);
                break;
            }
        }

        productRepository.save(product);

        return null;
    }
}
