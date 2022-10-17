package com.e.commerce.service;

import com.e.commerce.dto.PasswordChangeRequestDto;
import com.e.commerce.dto.SellerProductCustomizationDto;
import com.e.commerce.dto.converter.ProductDtoConverter;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.dto.product.ProductWithProductCustomizationDto;
import com.e.commerce.dto.seller.SellerRequestDto;
import com.e.commerce.dto.seller.SellerWithProductsDto;
import com.e.commerce.dto.seller.SellerWithoutProductsDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Address;
import com.e.commerce.model.ProductCustomization;
import com.e.commerce.model.Seller;
import com.e.commerce.repository.SellerRepository;
import com.e.commerce.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SellerService {

private  final SellerRepository sellerRepository;

private final AddressService addressService;

private final SellerDtoConverter sellerDtoConverter;

private final ProductDtoConverter productDtoConverter;

private final ProductCustomizationService productCustomizationService;

private final ProductService productService;

    public SellerService(SellerRepository sellerRepository, AddressService addressService, SellerDtoConverter sellerDtoConverter, ProductDtoConverter productDtoConverter, ProductCustomizationService productCustomizationService, ProductService productService) {
        this.sellerRepository = sellerRepository;
        this.addressService = addressService;
        this.sellerDtoConverter = sellerDtoConverter;
        this.productDtoConverter = productDtoConverter;
        this.productCustomizationService = productCustomizationService;
        this.productService = productService;
    }

    public Seller createAndSave(SellerRequestDto sellerRequestDto) {
        if (sellerRepository.existsByName(sellerRequestDto.getName())) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "There is already a seller with this name: " + sellerRequestDto.getName());
        }

        Seller seller = new Seller();
        seller.setName(sellerRequestDto.getName());
        seller.setEmail(sellerRequestDto.getEmail());
        seller.setPhoneNumber(sellerRequestDto.getPhoneNumber());
        seller.setAddress(addressService.createAndSaveAddress(sellerRequestDto.getSellerAddress()));
        seller.setPassword(PasswordUtil.encodePassword(sellerRequestDto.getEmail()));

        Seller savedSeller = this.save(seller);

        savedSeller.setPassword(null);

        return savedSeller;
    }

    public Seller save(Seller seller) {
        return sellerRepository.save(seller);
    }

    public SellerWithoutProductsDto getSellerWithoutProductById(Long sellerId) {
        return sellerDtoConverter.convertToSellerWithoutProductsDto(this.findSellerByIdOrElseThrow(sellerId));
    }

    public SellerWithoutProductsDto getSellerWithoutProductByName(String sellerName) {
        return sellerDtoConverter.convertToSellerWithoutProductsDto(this.findSellerByNameOrElseThrow(sellerName));
    }

    public SellerWithProductsDto getSellerWithProductById(Long sellerId) {
        return this.getSellerWithProduct(this.findSellerByIdOrElseThrow(sellerId));
    }

    public SellerWithProductsDto getSellerWithProductByName(String sellerName) {
        return this.getSellerWithProduct(this.findSellerByNameOrElseThrow(sellerName));
    }


    private SellerWithProductsDto getSellerWithProduct (Seller seller) {
        List<ProductWithProductCustomizationDto> productList = new ArrayList<>();
        productCustomizationService.findAllBySellerName(seller.getName())
                .forEach(productCustomization -> {
                    productService.findOptionalProductById(productCustomization.getProductId()).ifPresent(product -> {
                        productList.add(
                                productDtoConverter.convertToProductWithProductCustomizationDto(product, productCustomization));
                    });
        });

        return sellerDtoConverter.convertToSellerWithProductsDto(seller, productList);
    }

    public List<Seller> findAll() {
        return sellerRepository.findAll()
                .stream()
                .peek(seller -> seller.setPassword(null))
                .collect(Collectors.toList());
    }

    public Seller findSellerByIdOrElseThrow(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(
                        () ->  new DataNotFoundException("Seller not found by id :" + sellerId));
    }

    public Optional<Seller> findOptionalSellerByName(String sellerName) {
        return sellerRepository.findByName(sellerName);
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

    public Seller updateSeller(Long sellerId, SellerRequestDto sellerRequestDto) {
        Seller seller = this.findSellerByIdOrElseThrow(sellerId);

        sellerRequestDto.getSellerAddress().setTitle("official");

        Address updatedAddress = addressService.updateAddress(seller.getAddress().getId(), sellerRequestDto.getSellerAddress());
        seller.setName(sellerRequestDto.getName());
        seller.setEmail(sellerRequestDto.getEmail());
        seller.setPhoneNumber(sellerRequestDto.getPhoneNumber());
        seller.setAddress(updatedAddress);

        Seller savedSeller = this.save(seller);
        savedSeller.setPassword(null);

        return savedSeller;
    }

    public Seller deleteSeller(Long sellerId) {
        Seller seller = this.findSellerByIdOrElseThrow(sellerId);
        productCustomizationService.deleteAll(productCustomizationService.findAllBySellerName(seller.getName()));
        sellerRepository.delete(seller);
        return seller;
    }

    public void changeSellerPassword(Long sellerId, PasswordChangeRequestDto passwordChangeRequestDto) {
        if (!Objects.equals(passwordChangeRequestDto.getNewPassword(), passwordChangeRequestDto.getNewPasswordRepeat())) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Your new password does not match the password repetition.");
        }

        Seller seller = this.findSellerByIdOrElseThrow(sellerId);

        if (PasswordUtil.isPasswordMatch(passwordChangeRequestDto.getOldPassword(), seller.getPassword())) {
            seller.setPassword(PasswordUtil.encodePassword(passwordChangeRequestDto.getNewPassword()));
            this.save(seller);
        } else {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Your old password is wrong.");
        }
    }

    public SellerProductCustomizationDto sellerAddNewProduct(Long sellerId, SellerProductCustomizationDto requestDto) {

        if (!productService.isProductExistsById(requestDto.getProductId())) {
            throw new DataNotFoundException("Product not exists by id : " + requestDto.getProductId());
        }

        ProductCustomization productCustomization =
                productCustomizationService.createAndSave(this.findSellerByIdOrElseThrow(sellerId).getName(), requestDto);

        return new SellerProductCustomizationDto(productCustomization);
    }

    public SellerProductCustomizationDto sellerRemoveProduct(Long sellerId, Long productId) {

        ProductCustomization productCustomization =
                productCustomizationService.findBySellerIdAndProductIdOrElseThrow(this.findSellerByIdOrElseThrow(sellerId).getName(), productId);
        productCustomizationService.delete( productCustomization);
        return new SellerProductCustomizationDto(productCustomization);
    }
}
