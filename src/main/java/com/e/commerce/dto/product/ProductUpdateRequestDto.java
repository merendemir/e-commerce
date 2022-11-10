package com.e.commerce.dto.product;

import com.e.commerce.enums.ProductCategory;
import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {

    private String name;

    private ProductCategory productCategory;

    private String brand;

}
