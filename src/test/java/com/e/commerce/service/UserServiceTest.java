package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.UserCreateRequestDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.dto.converter.AddressDtoConverter;
import com.e.commerce.dto.converter.UserDtoConverter;
import com.e.commerce.enums.Role;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Address;
import com.e.commerce.model.User;
import com.e.commerce.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

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
        UserCreateRequestDto userCreateRequestDto = this.generateUserCreateRequestDto();
        UserDto userDto = this.generateUserDto();
        User user = this.generateUserFromUserCreateRequestDto(userCreateRequestDto);

        Mockito.when(userDtoConverter.createNewUserFromUserCreateRequestDto(userCreateRequestDto)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userDtoConverter.convertFromUserToUserDto(user)).thenReturn(userDto);

        UserDto result = userService.createAndSaveUser(userCreateRequestDto);

        Assert.assertEquals(userDto, result);

        Mockito.verify(userDtoConverter).createNewUserFromUserCreateRequestDto(userCreateRequestDto);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(userDtoConverter).convertFromUserToUserDto(user);
    }

    @Test
    public void saveUser() {
        User user = this.generateUserFromUserDto(this.generateUserDto());
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        Assert.assertEquals(user, result);

        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void whenGetUserAsDtoCalledWithExistUser_thenItShouldReturnUserDto() {
        UserDto userDto = this.generateUserDto();
        User user = this.generateUserFromUserDto(userDto);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userDtoConverter.convertFromUserToUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserAsDto(user.getId());

        Assert.assertEquals(result, userDto);

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userDtoConverter).convertFromUserToUserDto(user);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetUserAsDtoCalledWithNonExistUser_thenItShouldThrowDataNotFoundException() {
        long userId = -1;

        Mockito.when(userRepository.findById(userId)).thenThrow(new DataNotFoundException("User not found by id :" + userId));
        Assert.assertNull(userService.getUserAsDto(userId));

        Mockito.verifyNoInteractions(userDtoConverter);
    }

    @Test
    public void whenGetAllUserForAdminAndRepositoryDoesNotHaveUser_thenItShouldReturnEmptyList() {

        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> result = userService.getAllUserForAdmin();

        Assert.assertEquals(result, new ArrayList<>());

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void whenGetAllUserForAdminAndRepositoryHaveUser_thenItShouldReturnUserDtoList() {
        UserDto userDto = this.generateUserDto();
        UserDto userDto2 = this.generateUserDto2();

        User user = this.generateUserFromUserDto(userDto);
        User user2 = this.generateUserFromUserDto(userDto2);

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<User> result = userService.getAllUserForAdmin();

        Assert.assertEquals(result, List.of(user, user2));

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void whenFindUserByIdOrElseThrowCalledExistUser_thenItShouldReturnUser() {
        User user = this.generateUserFromUserDto(this.generateUserDto());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.findUserByIdOrElseThrow(user.getId());

        Assert.assertEquals(result, user);

        Mockito.verify(userRepository).findById(user.getId());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindUserByIdOrElseThrowCalledNotExistUser_thenItShouldThrowDataNotFoundException() {
        long userId = -1;

        Mockito.when(userRepository.findById(userId)).thenThrow(new DataNotFoundException("User not found by id :" + userId));
        Assert.assertNull(userService.findUserByIdOrElseThrow(userId));

        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    public void whenUpdateUserCalledExistUser_thenItShouldReturnUserDto() {
        User user = this.generateUserFromUserDto(this.generateUserDto());

        UserDto userDto = this.generateUserDto2();
        User updatedUser = this.generateUserFromUserDto(userDto);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        Mockito.when(userDtoConverter.convertFromUserToUserDto(updatedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(user.getId(), userDto);

        Assert.assertEquals(result, userDto);

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userRepository).save(updatedUser);
        Mockito.verify(userDtoConverter).convertFromUserToUserDto(updatedUser);
    }

    @Test
    public void whenDeleteUserCalledExistUser_thenItShouldReturnUser() {
        User user = this.generateUserFromUserDto(this.generateUserDto());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.deleteUser(user.getId());

        Assert.assertEquals(result, user);

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userRepository).delete(user);
    }

    @Test
    public void whenSaveUserAddressCalledAndUserAddressNotExists_thenItShouldReturnAddressDtoList() {
        UserDto userDto = this.generateUserDto();
        User user = this.generateUserFromUserDto(userDto);
        AddressDto addressDto = this.generateUserAddressDto();
        Address address = this.generateUserAddressFromAddressDto(addressDto);
        User userWithAddress = this.generateUserFromUserDtoWithAddress(userDto, List.of(address));

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(addressService.createAndSaveAddress(addressDto)).thenReturn(address);
        Mockito.when(userRepository.save(userWithAddress)).thenReturn(userWithAddress);
        Mockito.when(addressDtoConverter.convertToAddressDto(userWithAddress.getAddresses().get(0))).thenReturn(addressDto);

        List<AddressDto> result = userService.saveUserAddress(user.getId(), addressDto);

        Assert.assertEquals(result, List.of(addressDto));
        Assert.assertEquals(result.size(), 1);

        Mockito.verify(userRepository, times(2)).findById(user.getId());
        Mockito.verify(addressService).createAndSaveAddress(addressDto);
        Mockito.verify(userRepository).save(userWithAddress);
        Mockito.verify(addressDtoConverter).convertToAddressDto(address);
    }

    @Test
    public void whenSaveUserAddressCalledAndUserAddressExists_thenItShouldReturnAddressDtoList() {
        AddressDto userAddressDto = this.generateUserAddressDto();
        Address userAddress = this.generateUserAddressFromAddressDto(userAddressDto);
        User userWithAddress = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(userAddress));

        AddressDto addressDto2 = this.generateUserAddressDto2();
        Address address2 = this.generateUserAddressFromAddressDto2(addressDto2);

        User userAfterAddedAddress = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(userAddress, address2));

        Mockito.when(userRepository.findById(userWithAddress.getId())).thenReturn(Optional.of(userWithAddress));
        Mockito.when(addressService.createAndSaveAddress(addressDto2)).thenReturn(address2);
        Mockito.when(userRepository.save(userAfterAddedAddress)).thenReturn(userAfterAddedAddress);
        Mockito.when(addressDtoConverter.convertToAddressDto(userAfterAddedAddress.getAddresses().get(0))).thenReturn(userAddressDto);
        Mockito.when(addressDtoConverter.convertToAddressDto(userAfterAddedAddress.getAddresses().get(1))).thenReturn(addressDto2);

        List<AddressDto> result = userService.saveUserAddress(userWithAddress.getId(), addressDto2);

        Assert.assertEquals(result, List.of(userAddressDto, addressDto2));
        Assert.assertEquals(result.size(), 2);

        Mockito.verify(userRepository, times(2)).findById(userWithAddress.getId());
        Mockito.verify(userRepository, times(2)).findById(userAfterAddedAddress.getId());
        Mockito.verify(addressService).createAndSaveAddress(addressDto2);
        Mockito.verify(userRepository).save(userWithAddress);
        Mockito.verify(addressDtoConverter).convertToAddressDto(userWithAddress.getAddresses().get(0));
        Mockito.verify(addressDtoConverter).convertToAddressDto(userAfterAddedAddress.getAddresses().get(1));
    }

    @Test
    public void whenGetUserAddressCalled_thenItShouldReturnAddressDtoList() {
        AddressDto addressDto = this.generateUserAddressDto();
        Address address = this.generateUserAddressFromAddressDto(addressDto);

        AddressDto addressDto2 = this.generateUserAddressDto2();
        Address address2 = this.generateUserAddressFromAddressDto2(addressDto2);

        User userWithAddress = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(address, address2));

        Mockito.when(userRepository.findById(userWithAddress.getId())).thenReturn(Optional.of(userWithAddress));
        Mockito.when(addressDtoConverter.convertToAddressDto(userWithAddress.getAddresses().get(0))).thenReturn(addressDto);
        Mockito.when(addressDtoConverter.convertToAddressDto(userWithAddress.getAddresses().get(1))).thenReturn(addressDto2);

        List<AddressDto> result = userService.getUserAddress(userWithAddress.getId());

        Assert.assertEquals(result, List.of(addressDto, addressDto2));
        Assert.assertEquals(result.size(), 2);

        Mockito.verify(userRepository).findById(userWithAddress.getId());
        Mockito.verify(addressDtoConverter).convertToAddressDto(userWithAddress.getAddresses().get(0));
        Mockito.verify(addressDtoConverter).convertToAddressDto(userWithAddress.getAddresses().get(1));
    }

    @Test
    public void whenUpdateUserAddressCalledAndUserAddressExists_thenItShouldReturnAddressDtoList() {
        AddressDto addressDto = this.generateUserAddressDto();
        Address address = this.generateUserAddressFromAddressDto(addressDto);

        AddressDto addressDto2 = this.generateUserAddressDto2();
        Address updatedAddress = this.generateUserAddressFromAddressDto(addressDto2);

        User userWithAddress = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(address));

        Mockito.when(userRepository.findById(userWithAddress.getId())).thenReturn(Optional.of(userWithAddress));
        Mockito.when(addressService.updateAddress(address.getId(), addressDto2)).thenReturn(updatedAddress);
        Mockito.when(addressDtoConverter.convertToAddressDto(updatedAddress)).thenReturn(addressDto2);

        AddressDto result = userService.updateUserAddress(userWithAddress.getId(), address.getId(), addressDto2);

        Assert.assertEquals(addressDto2, result);

        Mockito.verify(userRepository).findById(userWithAddress.getId());
        Mockito.verify(addressService).updateAddress(address.getId(), addressDto2);
        Mockito.verify(addressDtoConverter).convertToAddressDto(updatedAddress);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateUserAddressCalledAndUserAddressNoTExists_thenItShouldThrowDataNotFoundException() {
        AddressDto addressDto = this.generateUserAddressDto();
        User user = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(generateUserAddressFromAddressDto(addressDto)));
        Long addressId = -1L;

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(addressService.updateAddress(addressId, addressDto)).thenThrow(new DataNotFoundException("Address not found by id :" + addressId));

        AddressDto result = userService.updateUserAddress(user.getId(), addressId, addressDto);

        Assert.assertNull(result);

        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(addressService).updateAddress(addressId, addressDto);
        Mockito.verifyNoInteractions(addressDtoConverter);
    }

    @Test
    public void whenDeleteUserAddressCalled_thenItShouldReturnAddressDtoList() {
        AddressDto addressDto = this.generateUserAddressDto();
        Address address = this.generateUserAddressFromAddressDto(addressDto);

        AddressDto addressDto2 = this.generateUserAddressDto2();
        Address address2 = this.generateUserAddressFromAddressDto2(addressDto2);

        User userWithAddress = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(address, address2));
        User userAfterUpdate = this.generateUserFromUserDtoWithAddress(this.generateUserDto(), List.of(address));

        Mockito.when(userRepository.findById(userWithAddress.getId())).thenReturn(Optional.of(userWithAddress));
        Mockito.when(userRepository.save(userAfterUpdate)).thenReturn(userAfterUpdate);
        Mockito.when(userRepository.findById(userAfterUpdate.getId())).thenReturn(Optional.of(userAfterUpdate));
        Mockito.when(addressDtoConverter.convertToAddressDto(userAfterUpdate.getAddresses().get(0))).thenReturn(addressDto);

        List<AddressDto> result = userService.deleteUserAddress(userWithAddress.getId(), address2.getId());

        Assert.assertEquals(List.of(addressDto), result);
        Assert.assertEquals(result.size(), 1);

        Mockito.verify(userRepository, times(2)).findById(userWithAddress.getId());
        Mockito.verify(addressDtoConverter).convertToAddressDto(userWithAddress.getAddresses().get(0));
    }

















    private UserCreateRequestDto generateUserCreateRequestDto() {
        return new UserCreateRequestDto(
                "test",
                "test",
                "test@gmail.com",
                "9054",
                "1234"
        );
    }

    private User generateUserFromUserCreateRequestDto(UserCreateRequestDto userDto) {
        return new User(
                135L,
                new Date(100),
                new Date(100),
                userDto.getName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                userDto.getPassword(),
                Role.USER,
                null
        );
    }

    private UserDto generateUserDto() {
        return new UserDto(
                "test",
                "test",
                "test@gmail.com",
                "9054"
        );
    }

    private User generateUserFromUserDto(UserDto userDto) {
        return new User(
                135L,
                new Date(100),
                new Date(100),
                userDto.getName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                null,
                Role.USER,
                null
        );
    }

    private User generateUserFromUserDtoWithAddress(UserDto userDto, List<Address> addresses) {
        return new User(
                135L,
                new Date(100),
                new Date(100),
                userDto.getName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                null,
                Role.USER,
                addresses
        );
    }

    private UserDto generateUserDto2() {
        return new UserDto(
                "test2",
                "test2",
                "test2@gmail.com",
                "4123"
        );
    }

    private AddressDto generateUserAddressDto() {
        return new AddressDto(
                "home",
                "TURKEY",
                "Istanbul",
                "center",
                "home home home"
        );
    }

    private Address generateUserAddressFromAddressDto(AddressDto addressDto) {
        return new Address(
                139L,
                new Date(1000),
                new Date(100),
                addressDto.getTitle(),
                addressDto.getCountry(),
                addressDto.getCity(),
                addressDto.getDistrict(),
                addressDto.getOpenAddress()
        );
    }

    private AddressDto generateUserAddressDto2() {
        return new AddressDto(
                "work",
                "USA",
                "NYC",
                "center",
                "NYC CENTER"
        );
    }

    private Address generateUserAddressFromAddressDto2(AddressDto addressDto) {
        return new Address(
                141L,
                new Date(1000),
                new Date(100),
                addressDto.getTitle(),
                addressDto.getCountry(),
                addressDto.getCity(),
                addressDto.getDistrict(),
                addressDto.getOpenAddress()
        );
    }

}