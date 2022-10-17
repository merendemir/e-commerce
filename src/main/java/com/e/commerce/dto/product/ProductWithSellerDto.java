package com.e.commerce.dto.product;

import com.e.commerce.dto.ProductCustomization.SellerProductCustomization;

import com.e.commerce.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ProductWithSellerDto {

    private Long productId;

    private String productName;

    private ProductCategory productCategory;

    private String productBrand;

    private List<String> productImages;

    private List<SellerProductCustomization> seller;
}
