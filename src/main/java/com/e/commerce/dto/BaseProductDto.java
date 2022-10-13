package com.e.commerce.dto;

import com.e.commerce.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductDto {

    @NotBlank
    private String name;

    @NotBlank
    private ProductCategory productCategory;

    private String brand;

}
