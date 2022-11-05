package com.e.commerce.service;

import com.e.commerce.enums.Role;
import com.e.commerce.exceptions.GenericException;
import com.e.commerce.model.Seller;
import com.e.commerce.model.User;
import com.e.commerce.util.JWTUtil;
import com.e.commerce.util.PasswordUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private JWTUtil jwtUtil;

    private UserService userService;

    private SellerService sellerService;

    private AuthService authService;

    @BeforeClass
    public static void staticSetUp() {
        mockStatic(PasswordUtil.class);
    }

    @Before
    public void setUp() {
        jwtUtil = mock(JWTUtil.class);
        userService = mock(UserService.class);
        sellerService = mock(SellerService.class);

        authService = new AuthService(
                jwtUtil,
                userService,
                sellerService);
    }

    @Test
    public void whenLoginCalledWithTruePassword_thenItShouldReturnAuthToken() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email.com")
                .password("password")
                .role(Role.USER)
                .build();

        String token = "authenticationToken";
        String password = "password";

        //when
        when(userService.findUserByEmailOrElseThrow(user.getEmail())).thenReturn(user);
        when(PasswordUtil.isPasswordMatch(password, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole())).thenReturn(token);

        //then
        String actual = authService.login(user.getEmail(), password);

        assertEquals(token, actual);

        verify(userService, times(1)).findUserByEmailOrElseThrow(user.getEmail());
        verify(jwtUtil, times(1)).generateToken(user.getId(), user.getEmail(), user.getRole());
    }

    @Test(expected = GenericException.class)
    public void whenLoginCalledWithWrongPassword_thenItShouldReturnAuthToken() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email.com")
                .password("password")
                .role(Role.USER)
                .build();

        String password = "wrong_password";

        //when
        when(userService.findUserByEmailOrElseThrow(user.getEmail())).thenReturn(user);
        when(PasswordUtil.isPasswordMatch(password, user.getPassword())).thenReturn(false);

        //then
        String actual = authService.login(user.getEmail(), password);

        assertNull(actual);

        verify(userService, times(1)).findUserByEmailOrElseThrow(user.getEmail());
    }

    @Test
    public void whenLoginForSellerCalledWithTruePassword_thenItShouldReturnAuthToken() {
        //given
        Seller seller = Seller.builder()
                .id(1L)
                .email("email.com")
                .password("password")
                .build();

        String token = "authenticationTokenForSeller";
        String password = "password";

        //when
        when(sellerService.findSellerByEmailOrElseThrow(seller.getEmail())).thenReturn(seller);
        when(PasswordUtil.isPasswordMatch(password, seller.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(seller.getId(), seller.getEmail(), Role.SELLER)).thenReturn(token);

        //then
        String actual = authService.loginForSeller(seller.getEmail(), password);

        assertEquals(token, actual);

        verify(sellerService, times(1)).findSellerByEmailOrElseThrow(seller.getEmail());
        verify(jwtUtil, times(1)).generateToken(seller.getId(), seller.getEmail(), Role.SELLER);
    }

    @Test(expected = GenericException.class)
    public void whenLoginForSellerCalledWithWrongPassword_thenItShouldReturnAuthToken() {
        //given
        Seller seller = Seller.builder()
                .id(1L)
                .email("email.com")
                .password("password")
                .build();

        String password = "wrong_password";

        //when
        when(sellerService.findSellerByEmailOrElseThrow(seller.getEmail())).thenReturn(seller);
        when(PasswordUtil.isPasswordMatch(password, seller.getPassword())).thenReturn(false);

        //then
        String actual = authService.loginForSeller(seller.getEmail(), password);

        assertNull(actual);

        verify(sellerService, times(1)).findSellerByEmailOrElseThrow(seller.getEmail());
    }

}