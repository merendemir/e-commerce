package com.e.commerce.service;

import com.e.commerce.enums.Role;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Seller;
import com.e.commerce.model.User;
import com.e.commerce.util.JWTUtil;
import com.e.commerce.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    private final SellerService sellerService;

    public String login(String email, String password) {
        User user = userService.findUserByEmailOrElseThrow(email);

        if (PasswordUtil.isPasswordMatch(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        } else {
            throw new GenericException(HttpStatus.BAD_REQUEST, "wrong password");
        }
    }


    public String loginForSeller (String email, String password) {
        Seller seller = sellerService.findSellerByEmailOrElseThrow(email);

        if (PasswordUtil.isPasswordMatch(password, seller.getPassword())) {
            return jwtUtil.generateToken(seller.getId(), seller.getEmail(), Role.SELLER);
        } else {
            throw new GenericException(HttpStatus.BAD_REQUEST, "wrong password");
        }
    }

}
