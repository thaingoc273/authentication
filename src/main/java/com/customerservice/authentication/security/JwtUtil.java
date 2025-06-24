package com.customerservice.authentication.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private SecretKey secretKey;
  @Autowired private JwtProperties jwtProperties;

  @PostConstruct
  public void init() {
    // this.secretKey = new
    // SecretKeySpec(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
    // SignatureAlgorithm.HS256.getJcaName());
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  // Generate a JWT token
  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtProperties.getExpirationTime()))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // Generate a JWT token with roles
  public String generateToken(String username, Set<String> roles) {
    return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtProperties.getExpirationTime()))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return userDetails.getUsername().equals(getUsernameFromToken(token));
    } catch (SecurityException
        | MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException e) {
      throw e;
    }
    // } catch (SecurityException e) {
    //     System.out.println("Invalid JWT signature: " + e.getMessage());
    // } catch (MalformedJwtException e) {
    //     System.out.println("Invalid JWT token: " + e.getMessage());
    // } catch (ExpiredJwtException e) {
    //     System.out.println("JWT token is expired: " + e.getMessage());
    // } catch (UnsupportedJwtException e) {
    //     System.out.println("JWT token is unsupported: " + e.getMessage());
    // } catch (IllegalArgumentException e) {
    //     System.out.println("JWT claims string is empty: " + e.getMessage());
    // }
    // return false;
  }
}
