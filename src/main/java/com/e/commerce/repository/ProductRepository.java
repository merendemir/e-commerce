package com.e.commerce.repository;

import com.e.commerce.enums.ProductCategory;
import com.e.commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    List<Product> findAllByBrand(String brand);

    List<Product> findAllByProductCategory(ProductCategory productCategory);

    boolean existsById(Long sellerId);
}
