package com.e.commerce.service;


import com.e.commerce.dto.*;
import com.e.commerce.dto.converter.ProductDtoConverter;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.enums.ProductCategory;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.PriceAndStock;
import com.e.commerce.model.Product;
import com.e.commerce.model.Seller;
import com.e.commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final SellerService sellerService;

    private final PriceAndStockService priceAndStockService;

    private final ProductDtoConverter productDtoConverter;

    private final SellerDtoConverter sellerDtoConverter;

    public ProductService(ProductRepository productRepository, SellerService sellerService, PriceAndStockService priceAndStockService, ProductDtoConverter productDtoConverter, SellerDtoConverter sellerDtoConverter) {
        this.productRepository = productRepository;
        this.sellerService = sellerService;
        this.priceAndStockService = priceAndStockService;
        this.productDtoConverter = productDtoConverter;
        this.sellerDtoConverter = sellerDtoConverter;
    }

    public BaseProductDto createAndSaveProduct(BaseProductDto baseProductDto) {
        Product savedProduct = this.saveProduct(new Product(baseProductDto));
        return productDtoConverter.convertToBaseProductDto(savedProduct);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public BaseProductDto getBaseProductById(Long productId) {
        return productDtoConverter.convertToBaseProductDto(this.findProductByIdOrElseThrow(productId));
    }

    public BaseProductDto getBaseProductByName(String productName) {
        return productDtoConverter.convertToBaseProductDto(this.findProductByProductNameOrElseThrow(productName));
    }


    public ProductWithSellerDto getProductWithSellerInformationByProductId(Long productId) {
        return this.getProductSellerInformation(this.findProductByIdOrElseThrow(productId));
    }

    public ProductWithSellerDto getProductWithSellerInformationByProductName(String productName) {
        return this.getProductSellerInformation(this.findProductByProductNameOrElseThrow(productName));
    }


    private ProductWithSellerDto getProductSellerInformation(Product product) {
        List<ProductSellerInformation> productSellerInformationList = new ArrayList<>();

        product.getSellers().forEach(seller -> {
            priceAndStockService.findPriceAndStockByProductAndSeller(seller, product).ifPresent(priceAndStock ->
                    productSellerInformationList.add(
                            new ProductSellerInformation(
                                    seller.getName(),
                                    priceAndStock.getPrice(),
                                    priceAndStock.getStock())));
        });

        return productDtoConverter.convertToProductWithSellerDto(product, productSellerInformationList);
    }

    public List<BaseProductDto> getAllProductAsBase() {
        return productRepository.findAll().stream()
                .map(productDtoConverter::convertToBaseProductDto)
                .collect(Collectors.toList());
    }

    public List<ProductWithSellerDto> getAllProductWithSellerInformation() {
        return productRepository.findAll().stream()
                .map(this::getProductSellerInformation)
                .collect(Collectors.toList());
    }

    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Product not found by id :" + productId));
    }

    public Product findProductByProductNameOrElseThrow(String name) {
        return productRepository.findByName(name)
                .orElseThrow(
                        () ->  new DataNotFoundException("Product not found by name :" + name));
    }

    public BaseProductDto updateProduct(Long productId, BaseProductDto baseProductDto) {
        Product product = this.findProductByIdOrElseThrow(productId);

        product.setName(baseProductDto.getName());
        product.setBrand(baseProductDto.getBrand());
        product.setProductCategory(baseProductDto.getProductCategory());

        return productDtoConverter.convertToBaseProductDto(this.saveProduct(product));
    }

    public Product deleteProduct(Long productId) {
        Product product = this.findProductByIdOrElseThrow(productId);
        productRepository.delete(product);
        priceAndStockService.deleteAll(priceAndStockService.findAllByProductId(product.getId()));
        return product;
    }

    public List<BaseProductDto> findAllProductByBrand(String brand) {
        return productRepository.findAllByBrand(brand).stream()
                .map(productDtoConverter::convertToBaseProductDto)
                .collect(Collectors.toList());
    }

    public List<BaseProductDto> findAllProductByCategory(ProductCategory productCategory) {
        return productRepository.findAllByProductCategory(productCategory).stream()
                .map(productDtoConverter::convertToBaseProductDto)
                .collect(Collectors.toList());
    }

    public List<SellerDto> findAllSellersByProductName(String productName) {
        Set<Seller> sellers = this.findProductByProductNameOrElseThrow(productName).getSellers();
        return sellers.stream()
                .map(sellerDtoConverter::convertToSellerDto)
                .collect(Collectors.toList());
    }

    public List<SellerDto> findAllSellersByProductId(Long productId) {
        Set<Seller> sellers = this.findProductByIdOrElseThrow(productId).getSellers();
        return sellers.stream()
                .map(sellerDtoConverter::convertToSellerDto)
                .collect(Collectors.toList());
    }

    public BaseProductDto addNewSellerByProductId(Long sellerId, SellerAddNewProductDto sellerAddNewProductDto) {
        Seller seller = sellerService.findSellerByIdOrElseThrow(sellerId);
        Product product = this.findProductByIdOrElseThrow(sellerAddNewProductDto.getProductId());

        PriceAndStock priceAndStock = priceAndStockService.createAndSavePriceAndStock(
                sellerAddNewProductDto.getPrice(),
                sellerAddNewProductDto.getStock(),
                seller,
                product);

        product.getPriceAndStocks().add(priceAndStock);
        product.getSellers().add(seller);
        return productDtoConverter.convertToBaseProductDto(this.saveProduct(product));
    }

    public BaseProductDto removeSellerByProductId(Long sellerId, Long productId) {
        Seller seller = sellerService.findSellerByIdOrElseThrow(sellerId);
        Product product = this.findProductByIdOrElseThrow(productId);

        Optional<PriceAndStock> priceAndStockOptional = priceAndStockService.findPriceAndStockByProductAndSeller(seller, product);
        if (priceAndStockOptional.isPresent()) {
            product.getPriceAndStocks().remove(priceAndStockOptional.get());
            product.getSellers().remove(seller);
        }

        return productDtoConverter.convertToBaseProductDto(this.saveProduct(product));
    }

}
