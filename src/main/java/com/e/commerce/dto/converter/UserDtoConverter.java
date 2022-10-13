package com.e.commerce.dto.converter;

import com.e.commerce.dto.UserCreateRequestDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.enums.Role;
import com.e.commerce.model.User;
import com.e.commerce.util.PasswordUtil;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertFromUserToUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User createNewUserFromUserCreateRequestDto(UserCreateRequestDto userCreateRequestDto) {
        return User.builder()
                .name(userCreateRequestDto.getName())
                .lastName(userCreateRequestDto.getLastName())
                .email(userCreateRequestDto.getEmail())
                .phoneNumber(userCreateRequestDto.getPhoneNumber())
                .password(PasswordUtil.encodePassword(userCreateRequestDto.getPassword()))
                .role(Role.USER)
                .build();
    }

}
