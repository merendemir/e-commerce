package com.e.commerce.service;


import com.e.commerce.dto.converter.ProductDtoConverter;
import com.e.commerce.dto.converter.SellerDtoConverter;
import com.e.commerce.dto.product.GetAllProductWithSellerDto;
import com.e.commerce.dto.seller.SellerAndProductCustomizationDto;
import com.e.commerce.model.Product;
import com.e.commerce.model.ProductCustomization;
import com.e.commerce.model.Seller;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private ProductService productService;

    private ProductCustomizationService productCustomizationService;

    private SellerService sellerService;

    private SellerDtoConverter sellerDtoConverter;

    private ProductDtoConverter productDtoConverter;

    private AdminService addressService;

    @Before
    public void setUp() {
        productService = mock(ProductService.class);
        productCustomizationService = mock(ProductCustomizationService.class);
        sellerService = mock(SellerService.class);
        sellerDtoConverter = mock(SellerDtoConverter.class);
        productDtoConverter = mock(ProductDtoConverter.class);

        addressService = new AdminService(
                productService,
                productCustomizationService,
                sellerService,
                sellerDtoConverter,
                productDtoConverter
        );
    }

    @Test
    public void whenGetAllProductWithSellerForAdminCalled_itShouldReturnGetAllProductWithSellerDtoList() {

        //given
        Product product1 = Product.builder()
                .id(1L)
                .name("product1")
                .build();


        ProductCustomization product1Customization1 = ProductCustomization.builder()
                .productId(product1.getId())
                .sellerName("seller1")
                .stock(10L)
                .price(BigDecimal.TEN)
                .build();

        ProductCustomization product1Customization2 = ProductCustomization.builder()
                .productId(product1.getId())
                .sellerName("seller2")
                .stock(10L)
                .price(BigDecimal.TEN)
                .build();

        Seller seller1 = Seller.builder()
                .id(1L)
                .name("seller1")
                .build();

        Seller seller2 = Seller.builder()
                .id(2L)
                .name("seller2")
                .build();

        SellerAndProductCustomizationDto sellerAndProductCustomizationDto1 = SellerAndProductCustomizationDto.builder()
                .sellerId(seller1.getId())
                .sellerName(seller1.getName())
                .sellerStock(product1Customization1.getStock())
                .sellerPrice(product1Customization1.getPrice())
                .build();

        SellerAndProductCustomizationDto sellerAndProductCustomizationDto2 = SellerAndProductCustomizationDto.builder()
                .sellerId(seller2.getId())
                .sellerName(seller2.getName())
                .sellerStock(product1Customization2.getStock())
                .sellerPrice(product1Customization2.getPrice())
                .build();

        List<SellerAndProductCustomizationDto> sellerAndProductCustomizationDtoList = List.of(sellerAndProductCustomizationDto1, sellerAndProductCustomizationDto2);

        GetAllProductWithSellerDto getAllProductWithSellerDto = GetAllProductWithSellerDto.builder()
                .productId(product1.getId())
                .productName(product1.getName())
                .productSellers(sellerAndProductCustomizationDtoList)
                .build();

        List<ProductCustomization> product1CustomizationList = List.of(product1Customization1, product1Customization2);
        List<GetAllProductWithSellerDto> expected = List.of(getAllProductWithSellerDto);


        //when
        when(productService.findAll()).thenReturn(List.of(product1));
        when(productCustomizationService.findAllByProductId(product1.getId())).thenReturn(product1CustomizationList);
        when(sellerService.findOptionalSellerByName(seller1.getName())).thenReturn(Optional.of(seller1));
        when(sellerService.findOptionalSellerByName(seller2.getName())).thenReturn(Optional.of(seller2));
        when(sellerDtoConverter.convertToSellerAndProductCustomizationDto(seller1, product1Customization1)).thenReturn(sellerAndProductCustomizationDto1);
        when(sellerDtoConverter.convertToSellerAndProductCustomizationDto(seller2, product1Customization2)).thenReturn(sellerAndProductCustomizationDto2);
        when(productDtoConverter.convertToAllProductWithSellerDto(product1, sellerAndProductCustomizationDtoList)).thenReturn(getAllProductWithSellerDto);

        //then
        List<GetAllProductWithSellerDto> actual = addressService.getAllProductWithSellerForAdmin();

        assertEquals(expected, actual);
        assertEquals(expected.size(), 1);

        verify(productService, times(1)).findAll();
        verify(productCustomizationService, times(1)).findAllByProductId(product1.getId());
        verify(sellerService, times(1)).findOptionalSellerByName(seller1.getName());
        verify(sellerService, times(1)).findOptionalSellerByName(seller2.getName());
        verify(sellerDtoConverter, times(1)).convertToSellerAndProductCustomizationDto(seller1, product1Customization1);
        verify(sellerDtoConverter, times(1)).convertToSellerAndProductCustomizationDto(seller2, product1Customization2);
        verify(productDtoConverter, times(1)).convertToAllProductWithSellerDto(product1, sellerAndProductCustomizationDtoList);

    }

}