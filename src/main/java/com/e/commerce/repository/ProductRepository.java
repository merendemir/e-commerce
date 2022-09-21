package com.e.commerce.repository;

import com.e.commerce.enums.ProductCategory;
import com.e.commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findAllByBrand(String brand);

    List<Product> findAllByProductCategory(ProductCategory productCategory);
}
