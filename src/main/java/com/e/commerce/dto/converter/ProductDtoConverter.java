package com.e.commerce.dto.converter;

import com.e.commerce.dto.BaseProductDto;
import com.e.commerce.dto.ProductSellerInformation;
import com.e.commerce.dto.ProductWithSellerDto;
import com.e.commerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoConverter {

    public BaseProductDto convertToBaseProductDto(Product product) {
        return new BaseProductDto(
                product.getName(),
                product.getProductCategory(),
                product.getBrand());
    }

    public ProductWithSellerDto convertToProductWithSellerDto(Product product, List<ProductSellerInformation> productSellerInformationList) {
        return new ProductWithSellerDto(
                product.getName(),
                product.getProductCategory(),
                product.getBrand(),
                productSellerInformationList);
    }
}
