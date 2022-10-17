package com.e.commerce.dto.seller;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SellerWithoutProductsDto {

    private Long sellerId;

    private String sellerName;

    private String sellerEmail;

    private String sellerCountry;

    private String sellerCity;
}
