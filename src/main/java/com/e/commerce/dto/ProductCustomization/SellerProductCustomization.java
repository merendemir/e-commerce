package com.e.commerce.dto.ProductCustomization;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class SellerProductCustomization {

    private String sellerName;

    private Long sellerStock;

    private BigDecimal sellerPrice;
}
