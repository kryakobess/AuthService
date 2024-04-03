package com.example.authservice.service.impl;

import com.example.authservice.config.JwtProperties;
import com.example.authservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateToken(Authentication authentication) {
        var token = Jwts.builder()
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .setSubject(authentication.getName())
                .compact();

        log.info("Generated JWT for {}. Token: {}", authentication.getName(), token);
        return token;
    }

    @Override
    public Authentication parseToken(String jwt) {
         var claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                 .build()
                 .parseClaimsJws(jwt)
                 .getBody();
        List<String> roles = (List<String>) claims.get("authorities");
        var authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    private PrivateKey getPrivateKey() {
        try {
            var cleanKey = jwtProperties.getPrivateKey().replaceAll("\\s+", "");
            var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(cleanKey));
            var keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("Error creating JWT token", e);
            return null;
        }
    }

    private PublicKey getPublicKey() {
        try {
            var cleanKey = jwtProperties.getPublicKey().replaceAll("\\s+", "");
            var keyFactory = KeyFactory.getInstance("RSA");
            var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cleanKey));
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("Error decrypting public key");
            return null;
        }
    }
}
