package com.e.commerce.dto.converter;

import com.e.commerce.dto.product.ProductWithProductCustomizationDto;
import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.dto.seller.SellerWithProductsDto;
import com.e.commerce.dto.seller.SellerWithoutProductsDto;
import com.e.commerce.model.ProductCustomization;
import com.e.commerce.model.Seller;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellerDtoConverter {

    public SellerAndProductCustomizationDto convertToSellerAndProductCustomizationDto(Seller seller, ProductCustomization productCustomization) {
        return SellerAndProductCustomizationDto.builder()
                .sellerId(seller.getId())
                .updatedAt(seller.getUpdatedAt())
                .createdAt(seller.getCreatedAt())
                .sellerName(seller.getName())
                .email(seller.getEmail())
                .phoneNumber(seller.getPhoneNumber())
                .sellerAddress(seller.getAddress())
                .sellerStock(productCustomization.getStock())
                .sellerPrice(productCustomization.getPrice())
                .build();
    }

    public SellerWithoutProductsDto convertToSellerWithoutProductsDto(Seller seller) {
        return SellerWithoutProductsDto.builder()
                .sellerId(seller.getId())
                .sellerName(seller.getName())
                .sellerEmail(seller.getEmail())
                .sellerCountry(seller.getAddress().getCity())
                .sellerCity(seller.getAddress().getCity())
                .build();
    }

    public SellerWithProductsDto convertToSellerWithProductsDto(Seller seller, List<ProductWithProductCustomizationDto> productList) {
        return SellerWithProductsDto.builder()
                .sellerId(seller.getId())
                .sellerName(seller.getName())
                .sellerEmail(seller.getEmail())
                .sellerCountry(seller.getAddress().getCity())
                .sellerCity(seller.getAddress().getCity())
                .products(productList)
                .build();
    }



}
