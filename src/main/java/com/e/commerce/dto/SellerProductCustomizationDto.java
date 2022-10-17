package com.e.commerce.dto;

import com.e.commerce.model.ProductCustomization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.customizers.ParameterCustomizer;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
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