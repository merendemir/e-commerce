package com.e.commerce.dto.product;

import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.enums.ProductCategory;
import com.e.commerce.model.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@ToString
public class GetAllProductWithSellerDto {

    private Long productId;

    private String productName;

    private ProductCategory productCategory;

    private String productBrand;

    private Set<Image> productImages;

    private List<SellerAndProductCustomizationDto> productSellers;
}
