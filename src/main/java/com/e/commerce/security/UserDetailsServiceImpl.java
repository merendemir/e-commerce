package com.e.commerce.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.e.commerce.model.Seller;
import com.e.commerce.model.User;
import com.e.commerce.service.SellerService;
import com.e.commerce.service.UserService;
import com.e.commerce.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final SellerService sellerService;

    private final JWTUtil jwtUtil;

    public UserDetailsServiceImpl(UserService userService, SellerService sellerService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.sellerService = sellerService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        DecodedJWT decodedJWT = jwtUtil.verifyJWT(token);

        String email = decodedJWT.getClaim("email").asString();
        String role = decodedJWT.getClaim("role").asString();
        String password ;

        if (role.equals("SELLER")) {
            password = sellerService.findSellerByEmailOrElseThrow(email).getPassword();
        } else {
            password = userService.findUserByEmailOrElseThrow(email).getPassword();
        }

        List<SimpleGrantedAuthority> roles = Stream.of(role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                roles);
    }
}
