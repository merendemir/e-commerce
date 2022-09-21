package com.e.commerce.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PriceAndStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    private Seller seller;

    public PriceAndStock(BigDecimal price, Long stock, Seller seller, Product product) {
        this.price = price;
        this.stock = stock;
        this.seller = seller;
        this.product = product;
    }
}
