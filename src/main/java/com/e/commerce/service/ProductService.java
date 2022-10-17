package com.e.commerce.service;


import com.e.commerce.dto.converter.ProductCustomizationConverter;
import com.e.commerce.dto.converter.ProductDtoConverter;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.dto.product.*;
import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.enums.ProductCategory;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Image;
import com.e.commerce.model.Product;
import com.e.commerce.model.Seller;
import com.e.commerce.repository.ProductRepository;
import com.e.commerce.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCustomizationService productCustomizationService;

    private final ProductDtoConverter productDtoConverter;

    private final ImagesService imagesService;

    private final ProductCustomizationConverter productCustomizationConverter;

    public ProductService(ProductRepository productRepository, ProductCustomizationService productCustomizationService, ProductDtoConverter productDtoConverter, ImagesService imagesService, ProductCustomizationConverter productCustomizationConverter) {
        this.productRepository = productRepository;
        this.productCustomizationService = productCustomizationService;
        this.productDtoConverter = productDtoConverter;
        this.imagesService = imagesService;
        this.productCustomizationConverter = productCustomizationConverter;
    }

    public Product createAndSave(ProductRequestDto requestDto) {

        Product product = new Product();

        product.setName(requestDto.getName());
        product.setProductCategory(requestDto.getProductCategory());
        product.setBrand(requestDto.getBrand());

        if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
            product.setImages(requestDto.getImages()
                    .stream()
                    .map(imagesService::createAndSaveImages)
                    .collect(Collectors.toSet()));
        }

        return this.save(product);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Boolean isProductExistsById(Long productId) {
        return productRepository.existsById(productId);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public ProductWithoutSellersDto getProductWithoutSellerById(Long productId) {
        return productDtoConverter.convertToProductWithoutSellersDto(this.findProductByIdOrElseThrow(productId));
    }

    public List<ProductWithoutSellersDto> getAllProductWithoutSellerByName(String productName) {
        return this.findAllByName(productName)
                .stream()
                .map(productDtoConverter::convertToProductWithoutSellersDto)
                .collect(Collectors.toList());

    }

    public List<Product> findAllByName(String productName) {
        return productRepository.findAllByName(productName);
    }

    public ProductWithSellerDto getProductWithSellerByProductId(Long productId) {
        return this.getProductWithSeller(this.findProductByIdOrElseThrow(productId));
    }

    public List<ProductWithSellerDto> getAllProductWithSellerByName(String productName) {
        return this.findAllByName(productName)
                .stream()
                .map(this::getProductWithSeller)
                .collect(Collectors.toList());
    }

    private ProductWithSellerDto getProductWithSeller(Product product) {
        return productDtoConverter.convertToProductWithSellerDto(
                product,
                productCustomizationService.findAllByProductId(product.getId())
                        .stream()
                        .map(productCustomizationConverter::convertToSellerProductCustomization)
                        .collect(Collectors.toList()));
    }

    public List<ProductWithoutSellersDto> getAllProductWithoutSeller() {
        return productRepository.findAll()
                .stream()
                .map(productDtoConverter::convertToProductWithoutSellersDto)
                .collect(Collectors.toList());
    }

    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Product not found by id :" + productId));
    }

    public Optional<Product> findOptionalProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public ProductWithoutSellersDto updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        Product product = this.findProductByIdOrElseThrow(productId);

        product.setName(requestDto.getName());
        product.setBrand(requestDto.getBrand());
        product.setProductCategory(requestDto.getProductCategory());

        return productDtoConverter.convertToProductWithoutSellersDto(this.save(product));
    }

    public Product deleteProduct(Long productId) {
        Product product = this.findProductByIdOrElseThrow(productId);
        productCustomizationService.deleteAll(productCustomizationService.findAllByProductId(product.getId()));
        productRepository.delete(product);
        return product;
    }

    public List<ProductWithoutSellersDto> findAllProductByBrand(String brand) {
        return productRepository.findAllByBrand(brand).stream()
                .map(productDtoConverter::convertToProductWithoutSellersDto)
                .collect(Collectors.toList());
    }

    public List<ProductWithoutSellersDto> findAllProductByCategory(ProductCategory productCategory) {
        return productRepository.findAllByProductCategory(productCategory).stream()
                .map(productDtoConverter::convertToProductWithoutSellersDto)
                .collect(Collectors.toList());
    }


    public Set<Image> getProductImage(Long productId) {
        return this.findProductByIdOrElseThrow(productId).getImages();
    }

    public Image addProductImage(Long productId, String url) {
        Product product = this.findProductByIdOrElseThrow(productId);

        Image savedImage = imagesService.saveImages(new Image(url));
        product.getImages().add(savedImage);

        this.save(product);

        return savedImage;
    }

    public Image updateProductImage(Long imageId, String url) {
        return imagesService.updateImage(imageId, url);
    }

    public void deleteProductImage(Long productId, Long imageId) {
        Image image = imagesService.findImageByIdOrElseThrow(imageId);
        this.findProductByIdOrElseThrow(productId).getImages()
                .removeIf(productImage -> productImage.getId().equals(image.getId()));
    }

}
