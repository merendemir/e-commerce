package com.e.commerce.dto;

import com.e.commerce.model.ProductCustomization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SellerProductCustomizationDto {

    private Long productId;

    private BigDecimal price;

    private Long stock;

    public SellerProductCustomizationDto(ProductCustomization productCustomization) {
        this.productId = productCustomization.getProductId();
        this.price = productCustomization.getPrice();
        this.stock = productCustomization.getStock();
    }
}
