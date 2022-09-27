package com.e.commerce.dto.converter;

import com.e.commerce.dto.UserDto;
import com.e.commerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertFromUserToUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber());
    }

    public User createNewUserFromUserDto(UserDto userDto) {
        return new User(userDto);
    }

}
