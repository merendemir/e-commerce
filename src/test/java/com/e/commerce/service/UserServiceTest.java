package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.UserCreateRequestDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.dto.converter.AddressDtoConverter;
import com.e.commerce.dto.converter.UserDtoConverter;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Address;
import com.e.commerce.model.User;
import com.e.commerce.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    private AddressService addressService;

    private AddressDtoConverter addressDtoConverter;

    private UserDtoConverter userDtoConverter;

    @Before
    public void setUp() throws Exception {
        userRepository = Mockito.mock(UserRepository.class);
        addressService = Mockito.mock(AddressService.class);
        addressDtoConverter = Mockito.mock(AddressDtoConverter.class);
        userDtoConverter = Mockito.mock(UserDtoConverter.class);

        userService = new UserService(
                userRepository,
                addressService,
                addressDtoConverter,
                userDtoConverter);
    }

    @Test
    public void whenCreateAndSaveUserCalled_thenItShouldReturnUserDto() {
        //given
        UserCreateRequestDto userCreateRequestDto = UserCreateRequestDto.builder()
                .name("name")
                .lastName("lastname")
                .password("password")
                .build();

        User user = User.builder()
                .id(1L)
                .name(userCreateRequestDto.getName())
                .lastName(userCreateRequestDto.getLastName())
                .password(userCreateRequestDto.getPassword())
                .build();

        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .build();

        //when
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userDtoConverter.convertFromUserToUserDto(user)).thenReturn(userDto);

        //then
        UserDto actual = userService.createAndSaveUser(userCreateRequestDto);

        assertEquals(userDto, actual);

        Mockito.verify(userRepository, times(1)).save(any(User.class));
        Mockito.verify(userDtoConverter, times(1)).convertFromUserToUserDto(user);
    }

    @Test
    public void whenSaveUserCalled_ThenItShoultReturnUser() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("lastname")
                .password("password")
                .build();

        //when
        when(userRepository.save(any(User.class))).thenReturn(user);

        //then
        User actual = userService.saveUser(user);

        assertEquals(user, actual);
        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void whenGetUserAsDtoCalledWithExistUser_thenItShouldReturnUserDto() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("lastname")
                .password("password")
                .build();

        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userDtoConverter.convertFromUserToUserDto(user)).thenReturn(userDto);

        //then
        UserDto actual = userService.getUserAsDto(user.getId());

        assertEquals(userDto, actual);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(userDtoConverter, times(1)).convertFromUserToUserDto(user);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetUserAsDtoCalledWithNonExistUser_thenItShouldThrowDataNotFoundException() {
        //given
        long userId = -1;

        //when
        when(userRepository.findById(userId)).thenThrow(new DataNotFoundException("User not found by id :" + userId));

        //then
        assertNull(userService.getUserAsDto(userId));

        verifyNoInteractions(userDtoConverter);
    }

    @Test
    public void whenGetAllUserForAdminAndRepositoryDoesNotHaveUser_thenItShouldReturnEmptyList() {
        //given
        List<User> userList = new ArrayList<>();

        //when
        when(userRepository.findAll()).thenReturn(userList);

        //then
        List<User> actual = userService.getAllUserForAdmin();

        assertEquals(userList, actual);
        assertEquals(0, actual.size());

        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    public void whenGetAllUserForAdminAndRepositoryHaveUser_thenItShouldReturnUserDtoList() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("name2")
                .build();

        List<User> userList = List.of(user, user2);

        //when
        when(userRepository.findAll()).thenReturn(userList);

        //then
        List<User> actual = userService.getAllUserForAdmin();

        assertEquals(userList, actual);

        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    public void whenFindUserByIdOrElseThrowCalledExistUser_thenItShouldReturnUser() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //then
        User actual = userService.findUserByIdOrElseThrow(user.getId());

        assertEquals(actual, user);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindUserByIdOrElseThrowCalledNotExistUser_thenItShouldThrowDataNotFoundException() {
        //given
        long userId = -1;

        //when
        when(userRepository.findById(userId)).thenThrow(new DataNotFoundException("User not found by id :" + userId));
        assertNull(userService.findUserByIdOrElseThrow(userId));

        Mockito.verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void whenFindUserByEmailOrElseThrowCalledExistUser_thenItShouldReturnUser() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("email")
                .build();

        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //then
        User actual = userService.findUserByEmailOrElseThrow(user.getEmail());

        assertEquals(user, actual);

        Mockito.verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindUserByEmailOrElseThrowCalledNotExistUser_thenItShouldThrowDataNotFoundException() {
        //given
        String email = "";

        //when
        when(userRepository.findByEmail(email)).thenThrow(new DataNotFoundException("User not found by email :" + email));

        assertNull(userService.findUserByEmailOrElseThrow(email));

        Mockito.verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void whenUpdateUserCalledExistUser_thenItShouldReturnUpdatedUserDto() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("lastname")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .name("name2")
                .lastName("lastname2")
                .build();

        UserDto userDto = UserDto.builder()
                .name(updatedUser.getName())
                .lastName(updatedUser.getLastName())
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userDtoConverter.convertFromUserToUserDto(updatedUser)).thenReturn(userDto);

        UserDto actual = userService.updateUser(user.getId(), userDto);

        assertEquals(userDto, actual);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(userRepository, times(1)).save(updatedUser);
        Mockito.verify(userDtoConverter, times(1)).convertFromUserToUserDto(updatedUser);
    }

    @Test
    public void whenDeleteUserCalledExistUser_thenItShouldReturnUser() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .lastName("lastname")
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User actual = userService.deleteUser(user.getId());

        assertEquals(user, actual);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void whenSaveUserAddressCalledAndUserAddressNotExists_thenItShouldReturnAddressDtoList() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        Address address = Address.builder()
                .id(1L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        User user = User.builder()
                .id(1L)
                .build();

        User userWithAddress = User.builder()
                .id(1L)
                .addresses(List.of(address))
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressService.createAndSaveAddress(addressDto)).thenReturn(address);
        when(userRepository.save(userWithAddress)).thenReturn(userWithAddress);
        when(addressDtoConverter.convertToAddressDto(userWithAddress.getAddresses().get(0))).thenReturn(addressDto);

        List<AddressDto> actual = userService.saveUserAddress(user.getId(), addressDto);

        assertEquals(List.of(addressDto), actual);
        assertEquals(1, actual.size());

        Mockito.verify(userRepository, times(2)).findById(user.getId());
        Mockito.verify(addressService, times(1)).createAndSaveAddress(addressDto);
        Mockito.verify(userRepository, times(1)).save(user);
        Mockito.verify(addressDtoConverter, times(1)).convertToAddressDto(address);
    }

    @Test
    public void whenSaveUserAddressCalledAndUserAddressExists_thenItShouldReturnAddressDtoList() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        Address address = Address.builder()
                .id(1L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        AddressDto addressDto2 = AddressDto.builder()
                .title("home2")
                .city("Ankara")
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        User user = User.builder()
                .id(1L)
                .addresses(List.of(address))
                .build();

        User userAfterAddedNewAddress = User.builder()
                .id(1L)
                .addresses(List.of(address, address2))
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressService.createAndSaveAddress(addressDto2)).thenReturn(address2);
        when(userRepository.save(userAfterAddedNewAddress)).thenReturn(userAfterAddedNewAddress);
        when(addressDtoConverter.convertToAddressDto(userAfterAddedNewAddress.getAddresses().get(0))).thenReturn(addressDto);
        when(addressDtoConverter.convertToAddressDto(userAfterAddedNewAddress.getAddresses().get(1))).thenReturn(addressDto2);

        //then
        List<AddressDto> actual = userService.saveUserAddress(userAfterAddedNewAddress.getId(), addressDto2);

        assertEquals(actual, List.of(addressDto, addressDto2));
        assertEquals(actual.size(), 2);

        Mockito.verify(userRepository, times(2)).findById(user.getId());
        Mockito.verify(addressService, times(1)).createAndSaveAddress(addressDto2);
        Mockito.verify(userRepository, times(1)).save(userAfterAddedNewAddress);
        Mockito.verify(addressDtoConverter, times(1)).convertToAddressDto(userAfterAddedNewAddress.getAddresses().get(0));
        Mockito.verify(addressDtoConverter, times(1)).convertToAddressDto(userAfterAddedNewAddress.getAddresses().get(1));
    }

    @Test
    public void whenGetUserAddressCalledAndUserAddressExists_thenItShouldReturnAddressDtoList() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        Address address = Address.builder()
                .id(1L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        AddressDto addressDto2 = AddressDto.builder()
                .title("home2")
                .city("Ankara")
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        User user = User.builder()
                .id(1L)
                .addresses(List.of(address, address2))
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressDtoConverter.convertToAddressDto(user.getAddresses().get(0))).thenReturn(addressDto);
        when(addressDtoConverter.convertToAddressDto(user.getAddresses().get(1))).thenReturn(addressDto2);

        //then
        List<AddressDto> actual = userService.getUserAddress(user.getId());

        assertEquals(List.of(addressDto, addressDto2), actual);
        assertEquals(actual.size(), 2);

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(addressDtoConverter).convertToAddressDto(user.getAddresses().get(0));
        Mockito.verify(addressDtoConverter).convertToAddressDto(user.getAddresses().get(1));
    }

    @Test
    public void whenGetUserAddressCalledAndUserAddressNonExists_thenItShouldReturnEmptyList() {
        //given
        User user = User.builder()
                .id(1L)
                .name("name")
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //then
        List<AddressDto> actual = userService.getUserAddress(user.getId());

        assertEquals(new ArrayList<>(), actual);
        assertEquals(actual.size(), 0);

        Mockito.verify(userRepository).findById(user.getId());
    }

    @Test
    public void whenUpdateUserAddressCalledAndUserAddressExists_thenItShouldReturnAddressDtoList() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        Address address = Address.builder()
                .id(1L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        AddressDto updatedAddressDto = AddressDto.builder()
                .title("home2")
                .city("Ankara")
                .build();

        Address updatedAddress = Address.builder()
                .id(1L)
                .title(updatedAddressDto.getTitle())
                .city(updatedAddressDto.getCity())
                .build();

        User user = User.builder()
                .id(1L)
                .addresses(List.of(address))
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressService.updateAddress(address.getId(), updatedAddressDto)).thenReturn(updatedAddress);
        when(addressDtoConverter.convertToAddressDto(updatedAddress)).thenReturn(updatedAddressDto);

        //then
        AddressDto actual = userService.updateUserAddress(user.getId(), address.getId(), updatedAddressDto);

        assertEquals(updatedAddressDto, actual);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(addressService, times(1)).updateAddress(address.getId(), updatedAddressDto);
        Mockito.verify(addressDtoConverter, times(1)).convertToAddressDto(updatedAddress);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateUserAddressCalledAndUserAddressNonExists_thenItShouldThrowDataNotFoundException() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        User user = User.builder()
                .id(1L)
                .name("name")
                .build();

        Long addressId = -1L;

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressService.updateAddress(addressId, addressDto)).thenThrow(new DataNotFoundException("Address not found by id :" + addressId));

        //then
        AddressDto actual = userService.updateUserAddress(user.getId(), addressId, addressDto);

        assertNull(actual);

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(addressService, times(1)).updateAddress(addressId, addressDto);
        verifyNoInteractions(addressDtoConverter);
    }

    @Test
    public void whenDeleteUserAddressCalled_thenItShouldReturnAddressDtoList() {
        //given
        AddressDto addressDto = AddressDto.builder()
                .title("home")
                .city("Istanbul")
                .build();

        Address address = Address.builder()
                .id(1L)
                .title(addressDto.getTitle())
                .city(addressDto.getCity())
                .build();

        User user = User.builder()
                .id(1L)
                .addresses(List.of(address))
                .build();

        User userAfterUpdate = User.builder()
                .id(1L)
                .addresses(new ArrayList<>())
                .build();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(userAfterUpdate)).thenReturn(userAfterUpdate);

        List<AddressDto> actual = userService.deleteUserAddress(user.getId(), address.getId());

        assertEquals(new ArrayList<>(), actual);
        assertEquals(actual.size(), 0);

        Mockito.verify(userRepository, times(2)).findById(user.getId());
    }
}