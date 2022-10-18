package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Address;
import com.e.commerce.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    private AddressService addressService;

    private AddressRepository addressRepository;

    @Before
    public void setUp() {
        addressRepository = mock(AddressRepository.class);

        addressService = new AddressService(addressRepository);
    }

    @Test
    public void whenCreateAndSaveAddressCalled_itShouldReturnAddress() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .city("Istanbul")
                .title("home")
                .build();
        Address address = Address.builder()
                .id(1L)
                .city(addressDto.getCity())
                .title(addressDto.getTitle())
                .build();

        //when
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        //then
        Address actual = addressService.createAndSaveAddress(addressDto);

        assertEquals(address, actual);

        verify(addressRepository, times(1)).save(any(Address.class));
    }


    @Test
    public void whenSaveAddressCalled_itShouldReturnAddress() {
        //given
        Address address = Address.builder()
                .city("Istanbul")
                .title("home")
                .build();

        //when
        when(addressRepository.save(address)).thenReturn(address);

        //then
        Address actual = addressService.saveAddress(address);

        assertEquals(address, actual);

        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void whenFindAddressByIdOrElseThrowCalledExistAddress_thenItShouldReturnAddress() {
        //given
        Address address = Address.builder()
                .id(1L)
                .city("Istanbul")
                .title("home")
                .build();

        //when
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        //then
        Address actual = addressService.findAddressByIdOrElseThrow(address.getId());

        assertEquals(address, actual);

        verify(addressRepository, times(1)).findById(address.getId());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindAddressByIdOrElseThrowCalledNotExistAddress_thenItShouldThrowDataNotFoundException() {
        //given
        Long addressId = -1L;

        //when
        when(addressRepository.findById(addressId)).thenThrow(new DataNotFoundException("Address not found by id :" + addressId));

        //then
        Address actual = addressService.findAddressByIdOrElseThrow(addressId);

        assertNull(actual);

        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    public void whenUpdateAddressCalled_thenItShouldReturnUpdatedAddress() {
        //given
        Address address = Address.builder()
                .id(1L)
                .city("Istanbul")
                .build();

        AddressDto addressDto = AddressDto.builder()
                .city("Ankara")
                .build();

        Address updatedAddress = Address.builder()
                .id(1L)
                .city(addressDto.getCity())
                .build();

        //when
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        when(addressRepository.save(updatedAddress)).thenReturn(updatedAddress);

        //then
        Address actual = addressService.updateAddress(address.getId(), addressDto);

        assertEquals(updatedAddress, actual);

        verify(addressRepository, times(1)).findById(address.getId());
        verify(addressRepository, times(1)).save(updatedAddress);
    }
}