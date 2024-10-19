package com.verysoft.classreservation.reservation.service;

import com.verysoft.classreservation.reservation.entity.UserEntity;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET = "c40af4c67eb119f651e3fe96ef8c7afbd3b5a45d83b7861a4b5aa237c668ec83";

    public String createJwt(UserEntity userEntity) {
        JWT jwt = new JWT()
                .setIssuedAt(ZonedDateTime.now())
                .setExpiration(ZonedDateTime.now().plusMinutes(60))
                .addClaim("username", userEntity.getUsername())
                .addClaim("roles", userEntity.getRoles());
        return JWT.getEncoder().encode(jwt, HMACSigner.newSHA256Signer(SECRET));
    }

    public UserEntity verifyJwt(final String token) {
        Verifier verifier = HMACVerifier.newVerifier(SECRET);

        JWT jwt = JWT.getDecoder().decode(token, verifier);

        Map<String, Object> allClaims = jwt.getAllClaims();
        final String username = (String) allClaims.get("username");
        final List<String> roleList = (List<String>) allClaims.get("roles");
        return new UserEntity(username, "", roleList);
    }

}
