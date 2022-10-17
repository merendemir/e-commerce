package com.e.commerce.dto.product;

import com.e.commerce.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ProductUpdateRequestDto {

    private String name;

    private ProductCategory productCategory;

    private String brand;

}
