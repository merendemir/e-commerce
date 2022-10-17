package com.e.commerce.dto.product;

import com.e.commerce.enums.ProductCategory;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
public class ProductRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private ProductCategory productCategory;

    private String brand;

    private Set<String> images;

}
