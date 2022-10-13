package com.e.commerce.service;

import com.e.commerce.dto.SellerDto;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Address;
import com.e.commerce.model.Seller;
import com.e.commerce.repository.SellerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {

private  final SellerRepository sellerRepository;

private final AddressService addressService;

private final SellerDtoConverter sellerDtoConverter;

    public SellerService(SellerRepository sellerRepository, AddressService addressService, SellerDtoConverter sellerDtoConverter) {
        this.sellerRepository = sellerRepository;
        this.addressService = addressService;
        this.sellerDtoConverter = sellerDtoConverter;
    }

    public SellerDto createAndSaveSeller(SellerDto sellerDto) {
        if (sellerRepository.existsByName(sellerDto.getName())) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "There is already a seller with this name: " + sellerDto.getName());
        }

        sellerDto.getSellerAddress().setTitle("official");
        Address savedSellerAddress = addressService.createAndSaveAddress(sellerDto.getSellerAddress());

        Seller savedSeller = this.saveSeller(new Seller(
                sellerDto.getName(),
                sellerDto.getEmail(),
                sellerDto.getPhoneNumber(),
                savedSellerAddress));

        return sellerDtoConverter.convertToSellerDto(savedSeller);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public SellerDto getSellerByNameAsDto(String name) {
        return sellerDtoConverter.convertToSellerDto(this.findSellerByNameOrElseThrow(name));
    }

    public SellerDto getSellerByIdAsDto(Long sellerId) {
        return sellerDtoConverter.convertToSellerDto(this.findSellerByIdOrElseThrow(sellerId));
    }

    public List<SellerDto> getAllSeller() {
        return sellerRepository.findAll().stream()
                .map(sellerDtoConverter::convertToSellerDto)
                .collect(Collectors.toList());
    }

    public Seller findSellerByIdOrElseThrow(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Seller not found by id :" + sellerId));
    }

    public Seller findSellerByEmailOrElseThrow(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(
                        () ->  new DataNotFoundException("Seller not found by email :" + email));
    }

    public Seller findSellerByNameOrElseThrow(String name) {
        return sellerRepository.findByName(name)
                .orElseThrow(
                        () ->  new DataNotFoundException("Seller not found by name :" + name));
    }

    public SellerDto updateSeller(Long sellerId, SellerDto sellerDto) {
        Seller seller = this.findSellerByIdOrElseThrow(sellerId);

        sellerDto.getSellerAddress().setTitle("official");

        Address updatedAddress = addressService.updateAddress(seller.getAddress().getId(), sellerDto.getSellerAddress());
        seller.setName(sellerDto.getName());
        seller.setEmail(sellerDto.getEmail());
        seller.setPhoneNumber(sellerDto.getPhoneNumber());
        seller.setAddress(updatedAddress);

        return sellerDtoConverter.convertToSellerDto(this.saveSeller(seller));
    }

    public Seller deleteSeller(Long sellerId) {
        Seller seller = this.findSellerByIdOrElseThrow(sellerId);
        sellerRepository.delete(seller);
        return seller;
    }
}
