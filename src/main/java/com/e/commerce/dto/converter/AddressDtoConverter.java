package com.e.commerce.dto.converter;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoConverter {

    public AddressDto convertToAddressDto(Address address) {
        return new AddressDto(
                address.getTitle(),
                address.getCountry(),
                address.getCity(),
                address.getDistrict(),
                address.getOpenAddress());
    }

}
