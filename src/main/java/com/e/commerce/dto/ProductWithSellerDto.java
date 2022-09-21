package com.e.commerce.dto;

import com.e.commerce.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithSellerDto {

    @NotNull
    private String name;

    @NotNull
    private ProductCategory productCategory;

    private String brand;

    private List<ProductSellerInformation> productSellerInformationList;

}
