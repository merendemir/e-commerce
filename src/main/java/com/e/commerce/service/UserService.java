package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.dto.converter.AddressDtoConverter;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.Address;
import com.e.commerce.model.User;
import com.e.commerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

private  final UserRepository userRepository;

private final AddressService addressService;

private final AddressDtoConverter addressDtoConverter;

    public UserService(UserRepository userRepository, AddressService addressService, AddressDtoConverter addressDtoConverter) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.addressDtoConverter = addressDtoConverter;
    }

    public UserDto createAndSaveUser(UserDto userDto) {
        User savedUser = this.saveUser(new User(userDto));
        return new UserDto(savedUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserDto getUserAsDto(Long userId) {
        return new UserDto(this.findUserByIdOrElseThrow(userId));
    }

    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () ->  new DataNotFoundException("User not found by id :" + userId));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = this.findUserByIdOrElseThrow(userId);

        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());

        return new UserDto(this.saveUser(user));
    }

    public User deleteUser(Long userId) {
        User user = this.findUserByIdOrElseThrow(userId);
        userRepository.delete(user);
        return user;
    }

    public List<AddressDto> saveUserAddress(Long userId, AddressDto addressDto) {

        User user = this.findUserByIdOrElseThrow(userId);

        List<Address> userAddresses = user.getAddresses();

        if (userAddresses == null) {
            userAddresses = new ArrayList<>();
        }

        Address savedAddress = addressService.createAndSaveAddress(addressDto);

        userAddresses.add(savedAddress);

        user.setAddresses(userAddresses);
        this.saveUser(user);

        return this.getUserAddress(userId);
    }

    public List<AddressDto> getUserAddress(Long userId) {
        return this.findUserByIdOrElseThrow(userId).getAddresses().stream()
                .map(addressDtoConverter::convertToAddressDto)
                .collect(Collectors.toList());
    }

    public List<AddressDto> updateUserAddress(Long userId, Long addressId, AddressDto addressDto) {
        List<Long> userAddressesId = this.findUserByIdOrElseThrow(userId).getAddresses().stream()
                .map(Address::getId)
                .toList();
        if (userAddressesId.isEmpty() || ! userAddressesId.toString().contains(addressId.toString())) {
            throw new DataNotFoundException("User Address not found by id :" + addressId);
        }

        addressService.updateAddress(addressId, addressDto);
        return getUserAddress(userId);
    }

    public List<AddressDto> deleteUserAddress(Long userId, Long addressId) {
        User user = this.findUserByIdOrElseThrow(userId);
        user.getAddresses().removeIf(address -> address.getId().equals(addressId));
        this.saveUser(user);
        addressService.deleteAddress(addressId);

        return this.getUserAddress(userId);
    }

}
