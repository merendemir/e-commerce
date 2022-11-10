package com.e.commerce.dto.converter;

import com.e.commerce.dto.ProductCustomization.SellerProductCustomization;
import com.e.commerce.dto.product.GetAllProductWithSellerDto;
import com.e.commerce.dto.product.ProductWithProductCustomizationDto;
import com.e.commerce.dto.product.ProductWithSellerDto;
import com.e.commerce.dto.product.ProductWithoutSellersDto;
import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.model.Image;
import com.e.commerce.model.Product;
import com.e.commerce.model.ProductCustomization;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductDtoConverter {

    public ProductWithoutSellersDto convertToProductWithoutSellersDto(Product product) {
        return ProductWithoutSellersDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productCategory(product.getProductCategory())
                .productBrand(product.getBrand())
                .productImages(this.getImageUrlList(product.getImages()))
                .build();
    }

    public ProductWithSellerDto convertToProductWithSellerDto(Product product, List<SellerProductCustomization> sellers) {
        return ProductWithSellerDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productCategory(product.getProductCategory())
                .productBrand(product.getBrand())
                .productImages(this.getImageUrlList(product.getImages()))
                .seller(sellers)
                .build();
    }

    public GetAllProductWithSellerDto convertToAllProductWithSellerDto(Product product, List<SellerAndProductCustomizationDto> sellers) {
        return GetAllProductWithSellerDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productCategory(product.getProductCategory())
                .productBrand(product.getBrand())
                .productImages(product.getImages())
                .productSellers(sellers)
                .build();
    }

    public ProductWithProductCustomizationDto convertToProductWithProductCustomizationDto(Product product, ProductCustomization productCustomization) {
        return ProductWithProductCustomizationDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productCategory(product.getProductCategory())
                .productBrand(product.getBrand())
                .productImages(this.getImageUrlList(product.getImages()))
                .sellerStock(productCustomization.getStock())
                .sellerPrice(productCustomization.getPrice())
                .build();
    }

    private List<String> getImageUrlList(Set<Image> imageList) {
        List<String> imageUrlList = new ArrayList<>();

        if (imageList != null && !imageList.isEmpty()) {
            imageUrlList = imageList.stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());
        }

        return imageUrlList;
    }

}
