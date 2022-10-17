package com.e.commerce.service;

import com.e.commerce.dto.SellerProductCustomizationDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.ProductCustomization;
import com.e.commerce.repository.ProductCustomizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductCustomizationService {

    private final ProductCustomizationRepository productCustomizationRepository;

    public ProductCustomizationService(ProductCustomizationRepository productCustomizationRepository) {
        this.productCustomizationRepository = productCustomizationRepository;
    }

    public ProductCustomization createAndSave(String sellerName, SellerProductCustomizationDto requestDto) {
        ProductCustomization productCustomization = new ProductCustomization();

        productCustomization.setSellerName(sellerName);
        productCustomization.setProductId(requestDto.getProductId());
        productCustomization.setPrice(requestDto.getPrice());
        productCustomization.setStock(requestDto.getStock());


       return this.save(productCustomization);
    }

    public ProductCustomization save(ProductCustomization productCustomization) {
        return productCustomizationRepository.save(productCustomization);
    }

    public ProductCustomization findBySellerIdAndProductIdOrElseThrow(String sellerName, Long productId) {
        return this.findOptionalBySellerIdAndProductId(sellerName, productId).orElseThrow(
                        () ->  new DataNotFoundException("Seller does not sell the product"));
    }

    public Optional<ProductCustomization> findOptionalBySellerIdAndProductId(String sellerName, Long productId) {
        return productCustomizationRepository.findBySellerNameAndProductId(sellerName, productId);
    }

    public List<ProductCustomization> findAllByProductId(Long productId) {
        return productCustomizationRepository.findAllByProductId(productId);
    }

    public List<ProductCustomization> findAllBySellerName(String sellerName) {
        return productCustomizationRepository.findAllBySellerName(sellerName);
    }

    public void deleteAll(List<ProductCustomization> productCustomizationList) {
        if(!productCustomizationList.isEmpty()) {
            productCustomizationRepository.deleteAll(productCustomizationList);
        }
    }

    public void delete(ProductCustomization productCustomization) {
        productCustomizationRepository.delete(productCustomization);
    }
}
