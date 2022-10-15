package com.e.commerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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
                .withClaim(role.getForClaims(), id)
                .withClaim("email", email)
                .withClaim("role", role.toString())
                .withExpiresAt(this.prepareExpiresDateByRole(role))
                .sign(Algorithm.HMAC256(KEY.getBytes()));
    }

    public DecodedJWT verifyJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(20).build();
        return verifier.verify(token);
    }

    public Map<String, Claim> getClaims(String token) {
        return JWT.decode(token).getClaims();
    }

    private Date prepareExpiresDateByRole(Role role) {
        long timeToAdded = switch (role) {
            case USER -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_USER;
            case ADMIN -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_ADMIN;
            case SELLER -> EXPIRES_ACCESS_TOKEN_MINUTE_FOR_SELLER;
        };

        return new Date(System.currentTimeMillis() + (timeToAdded * 60 * 1000));
    }

    public Long getUserIdFromToken(String token) {
        Claim userId = this.getClaims(token).get("userId");
        if (userId == null) {
            throw new GenericException(HttpStatus.NOT_ACCEPTABLE, "This action is for only users.");
        }

        return userId.asLong();
    }

    public Long getSellerIdFromToken(String token) {
        Claim sellerId = this.getClaims(token).get("sellerId");
        if (sellerId == null) {
            throw new GenericException(HttpStatus.NOT_ACCEPTABLE, "This action is for only sellers.");
        }

        return sellerId.asLong();
    }


}
