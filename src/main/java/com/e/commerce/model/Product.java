package com.e.commerce.model;

import com.e.commerce.enums.ProductCategory;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private ProductCategory productCategory;

    private String brand;

    private BigDecimal price;

    private Long stock;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Seller> sellers;
}
