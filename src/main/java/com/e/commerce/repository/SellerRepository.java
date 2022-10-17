package com.e.commerce.repository;

import com.e.commerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByName(String name);

    Optional<Seller> findByEmail(String email);

    Boolean existsByName(String name);

    boolean existsById(Long sellerId);
}
