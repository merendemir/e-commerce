package com.e.commerce.service;

import com.e.commerce.dto.SellerProductCustomizationDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Image;
import com.e.commerce.model.ProductCustomization;
import com.e.commerce.repository.ImageRepository;
import com.e.commerce.repository.ProductCustomizationRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductCustomizationServiceTest {

    private ProductCustomizationService productCustomizationService;

    private ProductCustomizationRepository productCustomizationRepository;

    @Before
    public void setUp() {
        productCustomizationRepository = mock(ProductCustomizationRepository.class);

        productCustomizationService = new ProductCustomizationService(productCustomizationRepository);
    }

    @Test
    public void whenCreateAndSaveCalled_itShouldReturnProductCustomization() {
        //given
        SellerProductCustomizationDto sellerProductCustomizationDto = SellerProductCustomizationDto.builder()
                .productId(1L)
                .price(BigDecimal.TEN)
                .stock(15L)
                .build();

        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName("seller1")
                .productId(sellerProductCustomizationDto.getProductId())
                .price(sellerProductCustomizationDto.getPrice())
                .stock(sellerProductCustomizationDto.getStock())
                .build();

        //when
        when(productCustomizationRepository.save(any(ProductCustomization.class))).thenReturn(productCustomization);

        //then
        ProductCustomization actual = productCustomizationService.createAndSave(productCustomization.getSellerName(), sellerProductCustomizationDto);

        assertEquals(productCustomization, actual);

        verify(productCustomizationRepository, times(1)).save(any(ProductCustomization.class));
    }


    @Test
    public void whenSaveCalled_itShouldReturnProductCustomization() {
        //given
        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName("seller1")
                .productId(1L)
                .price(BigDecimal.TEN)
                .stock(15L)
                .build();

        //when
        when(productCustomizationRepository.save(any(ProductCustomization.class))).thenReturn(productCustomization);

        //then
        ProductCustomization actual = productCustomizationService.save(productCustomization);

        assertEquals(productCustomization, actual);

        verify(productCustomizationRepository, times(1)).save(any(ProductCustomization.class));
    }

    @Test
    public void whenFindBySellerIdAndProductIdOrElseThrowCalledWithExists_itShouldReturnProductCustomization() {
        //given
        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName("seller1")
                .productId(1L)
                .price(BigDecimal.TEN)
                .stock(15L)
                .build();

        //when
        when(productCustomizationRepository.findBySellerNameAndProductId(
                productCustomization.getSellerName(),
                productCustomization.getProductId()))
                .thenReturn(Optional.of(productCustomization));

        //then
        ProductCustomization actual =
                productCustomizationService.findBySellerIdAndProductIdOrElseThrow(
                        productCustomization.getSellerName(),
                        productCustomization.getProductId());

        assertEquals(productCustomization, actual);

        verify(productCustomizationRepository,
                times(1)).findBySellerNameAndProductId(
                        productCustomization.getSellerName(),
                        productCustomization.getProductId());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindBySellerIdAndProductIdOrElseThrowCalledWithNonExists_itShouldThrow() {
        //given
        String sellerName = "seller1";
        Long productId = -1L;

        //when
        when(productCustomizationRepository.findBySellerNameAndProductId(sellerName, productId))
                .thenThrow(new DataNotFoundException("Seller does not sell the product"));

        //then
        ProductCustomization actual = productCustomizationService.findBySellerIdAndProductIdOrElseThrow(sellerName, productId);

        assertNull(actual);

        verify(productCustomizationService, times(1)).findBySellerIdAndProductIdOrElseThrow(sellerName, productId);
    }

    @Test
    public void whenFindOptionalBySellerIdAndProductIdCalled_itShouldReturnOptionalProductCustomization() {
        //given
        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName("seller1")
                .productId(1L)
                .price(BigDecimal.TEN)
                .stock(15L)
                .build();

        //when
        when(productCustomizationRepository.findBySellerNameAndProductId(
                productCustomization.getSellerName(),
                productCustomization.getProductId()))
                .thenReturn(Optional.of(productCustomization));

        //then
        Optional<ProductCustomization> actual =
                Optional.ofNullable(productCustomizationService.findBySellerIdAndProductIdOrElseThrow(
                        productCustomization.getSellerName(),
                        productCustomization.getProductId()));

        assertEquals(Optional.of(productCustomization), actual);

        verify(productCustomizationRepository,
                times(1)).findBySellerNameAndProductId(
                productCustomization.getSellerName(),
                productCustomization.getProductId());
    }

    @Test
    public void whenFindAllByProductIdCalled_itShouldReturnProductCustomizationList() {
        //given
        Long productId = 1L;

        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName("seller1")
                .productId(productId)
                .build();

        ProductCustomization productCustomization2 = ProductCustomization.builder()
                .sellerName("seller2")
                .productId(productId)
                .build();

        List<ProductCustomization> productCustomizationList = List.of(productCustomization, productCustomization2);

        //when
        when(productCustomizationRepository.findAllByProductId(productId))
                .thenReturn(productCustomizationList);

        //then
        List<ProductCustomization> actual = productCustomizationService.findAllByProductId(productId);

        assertEquals(productCustomizationList, actual);

        verify(productCustomizationRepository, times(1)).findAllByProductId(productId);
    }

    @Test
    public void whenFindAllBySellerNameCalled_itShouldReturnProductCustomizationList() {
        //given
        String sellerName = "seller1";

        ProductCustomization productCustomization = ProductCustomization.builder()
                .sellerName(sellerName)
                .productId(1L)
                .build();

        ProductCustomization productCustomization2 = ProductCustomization.builder()
                .sellerName(sellerName)
                .productId(2L)
                .build();

        List<ProductCustomization> productCustomizationList = List.of(productCustomization, productCustomization2);

        //when
        when(productCustomizationRepository.findAllBySellerName(sellerName))
                .thenReturn(productCustomizationList);

        //then
        List<ProductCustomization> actual = productCustomizationService.findAllBySellerName(sellerName);

        assertEquals(productCustomizationList, actual);

        verify(productCustomizationRepository, times(1)).findAllBySellerName(sellerName);
    }


}