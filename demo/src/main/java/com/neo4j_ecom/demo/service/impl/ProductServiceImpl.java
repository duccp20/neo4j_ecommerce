package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.request.ProductVariantRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    //===================== PRODUCT ====================
    @Override
    @Transactional
    public ProductResponse handleCreateProduct(ProductRequest request, List<MultipartFile> files) throws URISyntaxException, IOException {

        this.validateProductPrices(request);

        boolean existedProduct = productRepository.existsByName(request.getName().trim());

        if (existedProduct) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        boolean existedBrand = brandRepository.existsByName(request.getBrandName().trim());

        if (!existedBrand) {
            brandRepository.save(Brand.builder()
                    .name(request.getBrandName().trim())
                    .exclusiveShopId("999")
                    .build());
        }

        Product product = productMapper.toEntity(request);

        //variant
        if (request.getHasVariants() && request.getProductVariants() != null) {
            List<ProductVariant> productVariants = new ArrayList<>();
            for (ProductVariantRequest productVariantRequest : request.getProductVariants()) {

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

        //save product to category/collection
        if (request.getCategoryIds() != null) {
            for (String id : request.getCategoryIds()) {
                Category category = categoryRepository.findById(id).get();
                category.getProducts().add(savedProduct);
                categoryRepository.save(category);
            }
        }

        //save product to brand
        Brand brand = brandRepository.findByName(request.getBrandName().trim());
        brand.getProducts().add(savedProduct);
        brandRepository.save(brand);

        return productMapper.toResponse(savedProduct);
    }

    private void validateProductPrices(ProductRequest request) {
        if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0 ||
                request.getOriginalPrice().compareTo(request.getDiscountedPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }

    private void validateProductVariantPrices(ProductVariantRequest request) {
        if (request.getDiscountPrice() != null &&
                (request.getOriginalPrice().compareTo(request.getDiscountPrice()) >= 0 ||
                        request.getOriginalPrice().compareTo(request.getSellingPrice()) >= 0 ||
                        request.getDiscountPrice().compareTo(request.getSellingPrice()) >= 0)) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        } else if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }
    }

    @Override
    public ProductResponse handleUpdateProduct(String id, ProductRequest request) {


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
                Category categoryEntity = this.toCategory(category);
                categories.add(categoryEntity);
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
    public Void handleDeleteProduct(String id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.delete(product);

        return null;
    }

    @Override
    public List<ProductResponse> handleGetProductPopularBySoldQuantity() {

        List<Product> products = productRepository.findAll();


        products.forEach(p -> {
            if (p.getProductVariants() == null) {
                return;
            }
            long sum = p.getProductVariants().stream().mapToLong(ProductVariant::getSoldQuantity).sum();
            p.setSumSoldQuantity(sum);
        });
        products.sort((p1, p2) -> Long.compare(p2.getSumSoldQuantity(), p1.getSumSoldQuantity()));
        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Boolean handleProductExists(String name) {

        boolean existedProduct = productRepository.existsByName(name);

        return existedProduct;
    }

    @Override
    public ProductResponse handleGetProductById(String id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductResponse response = productMapper.toResponse(product);
        if (product.getProductVariants() != null) {

            Map<ProductType, Set<String>> options = new HashMap<>();
            for (ProductVariant variant : product.getProductVariants()) {
                for (VariantOption option : variant.getVariantOptions()) {
                    options.computeIfAbsent(option.getProductType(), k -> new HashSet<>())
                            .add(option.getValueName());
                }
            }

            response.setOptions(options);
        }

        log.info("product: {}", product);

        return response;

    }


    @Override
    public List<ProductResponse> handleGetAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }


    //==================PRODUCT IMAGES====================
    private List<String> handleCreateListImageFile(List<MultipartFile> files, Product product) throws URISyntaxException, IOException {

        List<String> images = new ArrayList<>();
        if (files != null) {

            for (MultipartFile file : files) {

                //local
                // String fileName = this.handleCreateProductImage(file);

                //firebase
                String url = this.handleCreateProductImageFirebase(file);

                images.add(url);
            }
        }

        return images;
    }

    @Override
    public String handleCreateProductImage(MultipartFile file) throws URISyntaxException {
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


    private String handleCreateProductImageFirebase(MultipartFile file) throws URISyntaxException, IOException {
        //validate file
        fileService.validateFile(file);

        String urlImage = fileService.storeFileFirebase(file, "products/images");

        log.info("url image: {}", urlImage);

        return urlImage;
    }

    @Override
    public List<String> HandleCreateProductImages(String productId, List<MultipartFile> files) throws URISyntaxException, IOException {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<String> newProductImage = new ArrayList<>();

        //current product image
        newProductImage.addAll(product.getProductImages());

        //new product image
        List<String> listProductImage = this.handleCreateListImageFile(files, product);
        newProductImage.addAll(listProductImage);

        product.setProductImages(newProductImage);
        productRepository.save(product);

        return listProductImage;
    }

    @Override
    public Void handleDeleteProductImage(String id, String imgUrl) {

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
    public Void handleSetPrimaryImage(String productId, String imgUrl) {

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


    //    ==================== MAPPER ======================
    private Category toCategory(CategoryResponse categoryResponse) {

        Category category = new Category();
        category.setId(categoryResponse.getId());
        category.setName(categoryResponse.getName().trim());
        category.setIcon(categoryResponse.getIcon());
        category.setLevel(categoryResponse.getLevel());

        return category;
    }

    private CategoryResponse toCategoryResponse(Category category) {

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());

        return categoryResponse;
    }

//    private ProductResponse toProductResponse(Product product) {
//        ProductResponse pRes = productMapper.toResponse(product);
//
//        List<CategoryResponse> categoryResponses = new ArrayList<>();
//        List<ReviewResponse> reviewResponses = new ArrayList<>();
//
//        for (Category category : product.getCategories()) {
//            categoryResponses.add(categoryMapper.toResponse(category));
//        }
//
//        for (ProductReview review : product.getReviews()) {
//
//            ReviewResponse reviewResponse = reviewMapper.toResponse(review);
//            reviewResponses.add(reviewResponse);
//        }
//
//
//        pRes.setCategories(categoryResponses);
//        pRes.setReviews(reviewResponses);
//        pRes.setImages(product.getProductImages());
//        return pRes;
//    }

}
