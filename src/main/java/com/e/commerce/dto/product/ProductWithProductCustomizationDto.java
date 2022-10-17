package com.e.commerce.dto.product;

import com.e.commerce.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@ToString
public class ProductWithProductCustomizationDto {

    private Long productId;

    private String productName;

    private ProductCategory productCategory;

    private String productBrand;

    private List<String> productImages;

    private Long sellerStock;

    private BigDecimal sellerPrice;
}
