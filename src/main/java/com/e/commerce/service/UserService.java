package com.e.commerce.service;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.PasswordChangeRequestDto;
import com.e.commerce.dto.UserCreateRequestDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.dto.converter.AddressDtoConverter;
import com.e.commerce.dto.converter.UserDtoConverter;
import com.e.commerce.enums.Role;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Address;
import com.e.commerce.model.User;
import com.e.commerce.repository.UserRepository;
import com.e.commerce.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

private final UserRepository userRepository;

private final AddressService addressService;

private final AddressDtoConverter addressDtoConverter;

private final UserDtoConverter userDtoConverter;


    public UserDto createAndSaveUser(UserCreateRequestDto userCreateRequestDto) {
        User user = new User();
        user.setName(userCreateRequestDto.getName());
        user.setLastName(userCreateRequestDto.getLastName());
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPhoneNumber(userCreateRequestDto.getPhoneNumber());
        user.setPassword(PasswordUtil.encodePassword(userCreateRequestDto.getPassword()));
        user.setRole(Role.USER);

        return userDtoConverter.convertFromUserToUserDto(this.saveUser(user));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserDto getUserAsDto(Long userId) {
        return userDtoConverter.convertFromUserToUserDto(this.findUserByIdOrElseThrow(userId));
    }

    public List<User> getAllUserForAdmin() {
        return userRepository.findAll();
    }

    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new DataNotFoundException("User not found by id :" + userId));
    }

    public User findUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->  new DataNotFoundException("User not found by email :" + email));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = this.findUserByIdOrElseThrow(userId);

        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());

        return userDtoConverter.convertFromUserToUserDto(this.saveUser(user));
    }

    public User deleteUser(Long userId) {
        User user = this.findUserByIdOrElseThrow(userId);
        userRepository.delete(user);
        return user;
    }

    public List<AddressDto> saveUserAddress(Long userId, AddressDto addressDto) {

        User user = this.findUserByIdOrElseThrow(userId);

        List<Address> userAddresses = new ArrayList<>();

        if (user.getAddresses() != null) {
            userAddresses.addAll(user.getAddresses());
        }

        Address savedAddress = addressService.createAndSaveAddress(addressDto);

        userAddresses.add(savedAddress);

        user.setAddresses(userAddresses);
        this.saveUser(user);

        return this.getUserAddress(userId);
    }

    public List<AddressDto> getUserAddress(Long userId) {
        List<Address> addressList = this.findUserByIdOrElseThrow(userId).getAddresses();
        if (addressList != null && !addressList.isEmpty()) {
            return addressList
                    .stream()
                    .map(addressDtoConverter::convertToAddressDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public AddressDto updateUserAddress(Long userId, Long addressId, AddressDto addressDto) {
        List<Address> addressList = this.findUserByIdOrElseThrow(userId).getAddresses();
        if (addressList == null || addressList.isEmpty()) {
            throw new DataNotFoundException("User Address not found by id :" + addressId);
        }

        List<Long> userAddressesId = addressList
                .stream()
                .map(Address::getId)
                .toList();

        if (userAddressesId.isEmpty() || !userAddressesId.toString().contains(addressId.toString())) {
            throw new DataNotFoundException("User Address not found by id :" + addressId);
        }

        return addressDtoConverter.convertToAddressDto(addressService.updateAddress(addressId, addressDto));
    }

    public List<AddressDto> deleteUserAddress(Long userId, Long addressId) {
        User user = this.findUserByIdOrElseThrow(userId);

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            List<Address> addressList = user.getAddresses()
                    .stream()
                    .filter(userAddress -> !Objects.equals(userAddress.getId(), addressId))
                    .toList();

            user.setAddresses(addressList);

            this.saveUser(user);
            addressService.deleteAddress(addressId);
        }

        return this.getUserAddress(userId);
    }

    public void changeUserPassword(Long userId, PasswordChangeRequestDto passwordChangeRequestDto) {
        if (!Objects.equals(passwordChangeRequestDto.getNewPassword(), passwordChangeRequestDto.getNewPasswordRepeat())) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Your new password does not match the password repetition.");
        }

        User user = this.findUserByIdOrElseThrow(userId);

        if (PasswordUtil.isPasswordMatch(passwordChangeRequestDto.getOldPassword(), user.getPassword())) {
            user.setPassword(PasswordUtil.encodePassword(passwordChangeRequestDto.getNewPassword()));
            this.saveUser(user);
        } else {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Your old password is wrong.");
        }
    }

}
