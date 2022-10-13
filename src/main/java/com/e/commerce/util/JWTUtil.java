package com.e.commerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.e.commerce.enums.Role;
import com.e.commerce.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
@Slf4j
public class JWTUtil {

    @Value("${jwt.key}")
    private String KEY;

    @Value("${jwt.user_expires_minute}")
    private long EXPIRES_ACCESS_TOKEN_MINUTE_FOR_USER;

    @Value("${jwt.admin_expires_minute}")
    private long EXPIRES_ACCESS_TOKEN_MINUTE_FOR_ADMIN;

    @Value("${jwt.seller_expires_minute}")
    private long EXPIRES_ACCESS_TOKEN_MINUTE_FOR_SELLER;


    public String generateToken(Long id, String email, Role role) {
        return JWT.create()
                .withClaim(role.getName() + "Id", id)
                .withClaim("email", email)
                .withClaim("role", role.toString())
                .withExpiresAt(this.getExpiresDateByRole(role))
                .sign(Algorithm.HMAC256(KEY.getBytes()));
    }


    private Date getExpiresDateByRole(Role role) {
        long timeToAdded = switch (role) {
            case USER -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_USER;
            case ADMIN -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_ADMIN;
            case SELLER -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_SELLER;
        };

        return new Date(System.currentTimeMillis() + (timeToAdded * 60 * 1000));
    }

    public Map<String, Claim> getClaims(String token) {
        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(20).build();
        try {
            return verifier.verify(token).getClaims();
        } catch (Exception e) {
            throw new GenericException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Long getUserIdByToken(String token) {
        return Long.parseLong(String.valueOf(this.getClaims(token).get("userId")));
    }


}
