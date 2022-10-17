package com.e.commerce.service;

import com.e.commerce.dto.converter.ProductDtoConverter;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.dto.product.GetAllProductWithSellerDto;
import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.model.Seller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    private final ProductService productService;

    private final ProductCustomizationService productCustomizationService;

    private final SellerService sellerService;

    private final SellerDtoConverter sellerDtoConverter;

    private final ProductDtoConverter productDtoConverter;

    public AdminService(ProductService productService, ProductCustomizationService productCustomizationService, SellerService sellerService, SellerDtoConverter sellerDtoConverter, ProductDtoConverter productDtoConverter) {
        this.productService = productService;
        this.productCustomizationService = productCustomizationService;
        this.sellerService = sellerService;
        this.sellerDtoConverter = sellerDtoConverter;
        this.productDtoConverter = productDtoConverter;
    }

    public List<GetAllProductWithSellerDto> getAllProductWithSellerForAdmin() {

        List<GetAllProductWithSellerDto> responseList = new ArrayList<>();

        productService.findAll().forEach(product -> {

            List<SellerAndProductCustomizationDto> sellerAndProductCustomizationDtoList = new ArrayList<>();

            productCustomizationService.findAllByProductId(product.getId()).forEach(productCustomization -> {

                Optional<Seller> optionalSeller = sellerService.findOptionalSellerByName(productCustomization.getSellerName());

                optionalSeller.ifPresent(seller -> sellerAndProductCustomizationDtoList.add(
                        sellerDtoConverter.convertToSellerAndProductCustomizationDto(seller, productCustomization)));

            });

            responseList.add(
                    productDtoConverter.convertToAllProductWithSellerDto(product, sellerAndProductCustomizationDtoList));
        });

        return responseList;
    }
}
