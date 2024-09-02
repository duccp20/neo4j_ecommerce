package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.dto.response.CategoryResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.dto.response.ReviewResponse;
import com.neo4j_ecom.demo.model.entity.*;
import com.neo4j_ecom.demo.model.mapper.CategoryMapper;
import com.neo4j_ecom.demo.model.mapper.ProductMapper;
import com.neo4j_ecom.demo.model.mapper.ProductReviewMapper;
import com.neo4j_ecom.demo.repository.*;
import com.neo4j_ecom.demo.service.*;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private final CategoryRepository categoryRepository;

    private final ProductImageService productImageService;

    private final ProductMapper productMapper;

    private final CategoryMapper categoryMapper;

    private final FileService fileService;

    private final ProductDimensionRepository productDimentionRepository;

    private final ProductDimensionService productDimensionService;

    private final CategoryService categoryService;

    private final ProductReviewMapper reviewMapper;


    //============== PRODUCT ====================
    @Override
    @Transactional
    public ProductResponse handleCreateProduct(ProductRequest request, List<MultipartFile> files) throws URISyntaxException, IOException {


        if (request.getOriginalPrice().compareTo(request.getSellingPrice()) > 0 ||
                request.getOriginalPrice().compareTo(request.getDiscountedPrice()) > 0) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_PRICES);
        }

        boolean existedProduct = productRepository.existsByName(request.getName().trim());

        if (existedProduct) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        Product product = productMapper.toEntity(request);
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription().trim());

        if (request.getProductDimension() != null) {
            ProductDimension productDimension = productDimensionService
                    .createProductDimension(request.getProductDimension());
            product.setProductDimension(productDimension);
        }

        List<String> listProductImage = this.handleCreateListImageFile(files, product);

        if (listProductImage.size() > 0) {
            product.setPrimaryImage(listProductImage.get(0));
        }

        if (!Collections.emptyList().equals(listProductImage)) {
            product.setProductImages(listProductImage);
        } else {
            product.setProductImages(null);
        }

        if (request.getCategoryIds() != null) {
            List<Category> categories = new ArrayList<>();
            for (String id : request.getCategoryIds()) {
                CategoryResponse categoryResponse = categoryService.handleGetCategoryById(id);
                Category categoryEntity = this.toCategory(categoryResponse);
                categories.add(categoryEntity);
            }
            product.setCategories(categories);
        }

        Product savedProduct = productRepository.save(product);

        ProductResponse response = productMapper.toResponse(savedProduct);


        if (product.getCategories() != null) {
            List<CategoryResponse> categoryResponses = product.getCategories()
                    .stream()
                    .map(category ->
                            CategoryResponse
                                    .builder()
                                    .id(category.getId())
                                    .name(category.getName())
                                    .build()).collect(Collectors.toList());
            response.setCategories(categoryResponses);
        }

        return response;

    }

    @Override
    public ProductResponse handleUpdateProduct(String id, ProductRequest request) {


        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("product: {}", product);

        boolean existedProduct = productRepository.existsByName(request.getName().trim());

        if (existedProduct) {
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

        ProductResponse response = productMapper.toResponse(savedProductAfterUpdate);

        if (product.getCategories() != null) {
            List<CategoryResponse> categoryResponses = product.getCategories().stream().map(category -> CategoryResponse.builder().id(category.getId()).name(category.getName()).build()).collect(Collectors.toList());

            response.setCategories(categoryResponses);
        }

        return response;

    }

    @Override
    public Void handleDeleteProduct(String id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.delete(product);

        return null;
    }

    @Override
    public List<ProductResponse> handleGetProducPopularBySoldQuantity() {

        List<Product> products = productRepository.findProductPopularBySoldQuantity();

        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        log.info("products in handleGetProducPopularBySoldQuantity: {}", products);

        List<ProductResponse> productResponseList = new ArrayList<>();

        products.forEach(product -> {

            ProductResponse pRes = productMapper.toResponse(product);

            log.info("{product response in handleGetProducPopularBySoldQuantity }", pRes);
            productResponseList.add(pRes);

        });

        return productResponseList;
    }

    @Override
    public Boolean handleProductExists(String name) {

        boolean existedProduct = productRepository.existsByName(name);

        return existedProduct;
    }

    @Override
    public ProductResponse handleGetProductById(String id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        log.info("product: {}", product);

        return this.toProductResponse(product);

    }

    @Override
    public List<ProductResponse> handleGetAllProducts() {

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        log.info("products in handleGetAllProducts: {}", products);

        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : products) {

            ProductResponse pRes = this.toProductResponse(product);
            productResponseList.add(pRes);

        }

        return productResponseList;


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

        if (categoryResponse.getChildren() != null) {

            List<Category> children = new ArrayList<>();

            for (CategoryResponse child : categoryResponse.getChildren()) {
                children.add(toCategory(child));
            }

            category.setChildren(children);
        }

        return category;
    }

    private CategoryResponse toCategoryResponse(Category category) {

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());

        return categoryResponse;
    }

    private ProductResponse toProductResponse(Product product) {
        ProductResponse pRes = productMapper.toResponse(product);

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        List<ReviewResponse> reviewResponses = new ArrayList<>();

        for (Category category : product.getCategories()) {
            categoryResponses.add(categoryMapper.toCategoryResponse(category));
        }

        for (ProductReview review : product.getReviews()) {

            ReviewResponse reviewResponse = reviewMapper.toResponse(review);
            reviewResponses.add(reviewResponse);
        }


        pRes.setCategories(categoryResponses);
        pRes.setReviews(reviewResponses);
        pRes.setImages(product.getProductImages());
        return pRes;
    }

}
