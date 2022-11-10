package com.e.commerce.dto.converter;

import com.e.commerce.dto.UserDto;
import com.e.commerce.model.User;
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

}
