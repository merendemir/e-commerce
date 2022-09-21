package com.e.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerAddNewProductDto {

    private Long sellerId;

    private Long productId;

    private BigDecimal price;

    private Long stock;
}
