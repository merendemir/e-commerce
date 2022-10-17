package com.e.commerce.dto.seller;
import com.e.commerce.dto.product.ProductWithProductCustomizationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class SellerWithProductsDto {

    private Long sellerId;

    private String sellerName;

    private String sellerEmail;

    private String sellerCountry;

    private String sellerCity;

    private List<ProductWithProductCustomizationDto> products;
}
