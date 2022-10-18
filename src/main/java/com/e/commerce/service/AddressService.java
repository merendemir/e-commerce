package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Address;
import com.e.commerce.repository.AddressRepository;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

private  final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAndSaveAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setTitle(addressDto.getTitle());
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setDistrict(addressDto.getDistrict());
        address.setOpenAddress(addressDto.getOpenAddress());

       return this.saveAddress(address);
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address findAddressByIdOrElseThrow(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Address not found by id :" + addressId));
    }

    public Address updateAddress(Long addressId, AddressDto addressDto) {
        Address address = this.findAddressByIdOrElseThrow(addressId);
        address.setTitle(addressDto.getTitle());
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setDistrict(addressDto.getDistrict());
        address.setOpenAddress(addressDto.getOpenAddress());

        return this.saveAddress(address);
    }
    public void deleteAddress(Long addressId) {
        Address address = this.findAddressByIdOrElseThrow(addressId);
        addressRepository.delete(address);
    }
}
