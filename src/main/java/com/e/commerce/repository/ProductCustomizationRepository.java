package com.e.commerce.repository;

import com.e.commerce.model.ProductCustomization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCustomizationRepository extends JpaRepository<ProductCustomization, Long> {

    Optional<ProductCustomization> findBySellerNameAndProductId(String sellerName, Long productId);

    List<ProductCustomization> findAllBySellerName(String sellerName);

    List<ProductCustomization> findAllByProductId(Long productId);


}
