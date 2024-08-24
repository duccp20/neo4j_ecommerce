package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductBannerRequest;
import com.neo4j_ecom.demo.model.dto.response.ProductBannerResponse;
import com.neo4j_ecom.demo.model.dto.response.ProductResponse;
import com.neo4j_ecom.demo.model.entity.Product;
import com.neo4j_ecom.demo.model.entity.ProductBanner;
import com.neo4j_ecom.demo.repository.ProductBannerRepository;
import com.neo4j_ecom.demo.repository.ProductRepository;
import com.neo4j_ecom.demo.service.FileService;
import com.neo4j_ecom.demo.service.ProductBannerService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductBannerServiceImpl implements ProductBannerService {

    private final ProductRepository productRepository;

    private final FileService fileService;

    private final ProductBannerRepository productBannerRepository;

    @Value("${file.image.folder.product-banner}")
    private String folder;

    @Value("${file.image.base-uri}")
    private String baseURI;

    @Override
    @Transactional
    public ProductBannerResponse handleCreateBanner(ProductBannerRequest request, String productId, List<MultipartFile> files) throws URISyntaxException {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));


        ProductBanner productBanner = this.toProductBanner(request);
        productBanner.setProductName(product.getName());

        if (files != null && files.size() > 0) {

            log.info("folder {}", folder);

            //create folder if not exists
            fileService.createUploadedFolder(baseURI + folder);


            List<String> bannerImages = new ArrayList<>();
            for (MultipartFile file : files) {

                //validate file
                fileService.validateFile(file);

                //store file
                String bannerImage = fileService.storeFile(file, folder);

                String url = baseURI + folder + "/" + bannerImage;
                //add banner image
                bannerImages.add(url);

                String finalNameTrimmed = file.getOriginalFilename().replaceAll("\\s", "");

                log.info("file name in handleCreateBanner {}", finalNameTrimmed);
                //set primary banner
                if (Objects.equals(finalNameTrimmed, request.getPrimaryBanner())) {
                    productBanner.setPrimaryBanner(bannerImage);
                }

            }

            //set banner images
            if (bannerImages.size() > 0) {
                productBanner.setBannerImages(bannerImages);
            }

            //set primary banner if only one product banner
            if (productBanner.getBannerImages().size() == 1) {
                productBanner.setPrimaryBanner(productBanner.getBannerImages().get(0));
            }
        }

        List<ProductBanner> productBannerList = product.getProductBanners();
        productBannerList.add(productBanner);

        productRepository.save(product);

        return this.toProductBannerResponse(productBanner);
    }

    @Override
    public List<ProductBannerResponse> handleGetBannersByProductId(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductBanner> productBanners = product.getProductBanners();

        if (productBanners != null && productBanners.size() > 0) {

            List<ProductBannerResponse> productBannerResponses = new ArrayList<>();
            for (ProductBanner banner : productBanners) {
                productBannerResponses.add(this.toProductBannerResponse(banner));
            }
            return productBannerResponses;

        } else {

            throw new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND);
        }
    }

    @Override
    public ProductBannerResponse handleUpdateBanner(String productId, String bannerId, ProductBannerRequest request, List<MultipartFile> files) throws URISyntaxException {


        ProductBanner productBanner = productBannerRepository.findById(bannerId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductBanner productBannerUpdate = this.toUpdateProductBanner(productBanner, request);

        List<String> bannerImages = new ArrayList<>();
        if (files != null && files.size() > 0) {

            log.info("folder {}", folder);

            for (MultipartFile file : files) {
                //validate file
                fileService.validateFile(file);
                //store file
                String bannerImage = fileService.storeFile(file, folder);

                String url = baseURI + folder + "/" + bannerImage;
                //add banner image
                bannerImages.add(url);
            }

            if (bannerImages.size() > 0) {
                productBannerUpdate.setBannerImages(bannerImages);
            }

            if (productBannerUpdate.getBannerImages().size() == 1) {
                productBannerUpdate.setPrimaryBanner(productBannerUpdate.getBannerImages().get(0));
            }
        }

        List<ProductBanner> productBannerList = product.getProductBanners();

        productBannerList.add(productBannerUpdate);

        productRepository.save(product);

        return this.toProductBannerResponse(productBannerUpdate);
    }

    private ProductBanner toUpdateProductBanner(ProductBanner productBanner, ProductBannerRequest request) {

        if (request.getPrimaryBanner() != null) {
            productBanner.setPrimaryBanner(request.getPrimaryBanner());
        }

        if (request.getLinkUrl() != null) {
            productBanner.setLinkUrl(request.getLinkUrl());
        }

        if (request.getTitle() != null) {
            productBanner.setTitle(request.getTitle());
        }

        return productBanner;
    }

    @Override
    public Void handleDeleteBannerByProductId(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getProductBanners() == null) {
            throw new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND);
        }

        product.setProductBanners(null);
        productRepository.save(product);

        return null;
    }

    @Override
    public ProductBannerResponse handleGetBannerById(String bannerId) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        return this.toProductBannerResponse(productBanner);
    }


    @Override
    public Void handleDeleteBannerById(String bannerId) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        productBannerRepository.delete(productBanner);

        return null;
    }

    @Override
    public Void handleDeleteBannerImage(String bannerId, String imgUrl) {

        ProductBanner productBanner = productBannerRepository.findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        List<String> bannerImages = productBanner.getBannerImages();

        for (String s : bannerImages) {
            if (s.equals(imgUrl)) {
                bannerImages.remove(s);
                break;
            }
        }
        productBanner.setBannerImages(bannerImages);
        productBannerRepository.save(productBanner);
        return null;
    }

    @Override
    public ProductBannerResponse handleUpdateBannerFiles(String bannerId, List<MultipartFile> files) throws URISyntaxException {

        ProductBanner productBanner = productBannerRepository
                .findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        List<String> bannerImages = new ArrayList<>();
        if (files != null && files.size() > 0) {

            log.info("folder {}", folder);

            for (MultipartFile file : files) {
                //validate file
                fileService.validateFile(file);
                //store file
                String bannerImage = fileService.storeFile(file, folder);

                String url = baseURI + folder + "/" + bannerImage;
                //add banner image
                bannerImages.add(url);
            }

            if (bannerImages.size() > 0) {
                productBanner.setBannerImages(bannerImages);
            }

            if (productBanner.getBannerImages().size() == 1) {
                productBanner.setPrimaryBanner(productBanner.getBannerImages().get(0));
            }
        }

        productBannerRepository.save(productBanner);


        return this.toProductBannerResponse(productBanner);
    }

    @Override
    public ProductBannerResponse handleUpdateBannerPrimary(String bannerId, String url) {

        ProductBanner productBanner = productBannerRepository
                .findById(bannerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_BANNER_NOT_FOUND));

        productBanner.setPrimaryBanner(url);
        productBannerRepository.save(productBanner);

        return this.toProductBannerResponse(productBanner);

    }


    private ProductBanner toProductBanner(ProductBannerRequest request) {

        return ProductBanner.builder().title(request.getTitle()).linkUrl(request.getLinkUrl()).build();
    }

    private ProductBannerResponse toProductBannerResponse(ProductBanner productBanner) {

        return ProductBannerResponse.builder().id(productBanner.getId()).title(productBanner.getTitle()).linkUrl(productBanner.getLinkUrl()).productName(productBanner.getProductName()).bannerImages(productBanner.getBannerImages()).primaryBanner(productBanner.getPrimaryBanner()).build();

    }


}

