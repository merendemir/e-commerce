package com.e.commerce.dto.converter;

import com.e.commerce.dto.SellerDto;
import com.e.commerce.model.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerDtoConverter {

    private final AddressDtoConverter addressDtoConverter;

    public SellerDtoConverter(AddressDtoConverter addressDtoConverter) {
        this.addressDtoConverter = addressDtoConverter;
    }

    public SellerDto convertToSellerDto(Seller seller) {
        return new SellerDto(
                seller.getName(),
                seller.getEmail(),
                seller.getPhoneNumber(),
                addressDtoConverter.convertToAddressDto(seller.getAddress()));
    }

}
