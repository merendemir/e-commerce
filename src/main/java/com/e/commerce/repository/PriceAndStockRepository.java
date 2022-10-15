package com.e.commerce.repository;

import com.e.commerce.model.PriceAndStock;
import com.e.commerce.model.Product;
import com.e.commerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceAndStockRepository extends JpaRepository<PriceAndStock, Long> {

    Optional<PriceAndStock> findBySellerAndProduct(Seller seller, Product product);

    List<PriceAndStock> findAllByProduct_Id(Long productId);

    List<PriceAndStock> findAllBySeller_Id(Long productId);
}
