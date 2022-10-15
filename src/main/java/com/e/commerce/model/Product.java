package com.e.commerce.model;

import com.e.commerce.dto.BaseProductDto;
import com.e.commerce.enums.ProductCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;
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

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private String brand;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Seller> sellers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PriceAndStock> priceAndStocks;

    public Product(BaseProductDto baseProductDto) {
        this.name = baseProductDto.getName();
        this.productCategory = baseProductDto.getProductCategory();
        this.brand = baseProductDto.getBrand();
    }
}
