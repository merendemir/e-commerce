package com.e.commerce.dto.seller;

import com.e.commerce.model.Address;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@Builder
@Getter
@ToString
public class SellerAndProductCustomizationDto {

    private Long sellerId;

    private Date updatedAt;

    private Date createdAt;

    private String sellerName;

    private String email;

    private String phoneNumber;

    private Address sellerAddress;

    private Long sellerStock;

    private BigDecimal sellerPrice;

}
