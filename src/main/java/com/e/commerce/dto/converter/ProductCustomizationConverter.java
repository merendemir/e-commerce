package com.e.commerce.dto.converter;

import com.e.commerce.dto.ProductCustomization.SellerProductCustomization;
import com.e.commerce.model.ProductCustomization;
import org.springframework.stereotype.Component;

@Component
public class ProductCustomizationConverter {

    public SellerProductCustomization convertToSellerProductCustomization(ProductCustomization productCustomization) {

        return SellerProductCustomization.builder()
                .sellerName(productCustomization.getSellerName())
                .sellerPrice(productCustomization.getPrice())
                .sellerStock(productCustomization.getStock())
                .build();
    }


}
