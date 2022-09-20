package com.e.commerce.service;

import com.e.commerce.dto.UserDto;
import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.User;
import com.e.commerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

private  final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public void testSave() {
        Long start = new Date().getTime();
        for(int i = 0; i < 10000; i++) {
            User user = new User();
            user.setName("eren");
            user.setLastName("demir");
            user.setEmail("test");
            user.setPhoneNumber("123");
            userRepository.save(user);
        }
        Long end = new Date().getTime();

        System.out.println("save " + (end-start));
    }

    public void testGet() {
        Long start = new Date().getTime();

        userRepository.findAll().forEach(user ->{
            userRepository.findById(user.getId());
        });

        Long end = new Date().getTime();
        System.out.println("get " + (end-start));
    }

}
