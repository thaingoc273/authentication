package com.customerservice.authentication.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
  private String secret;
  private long expirationTime;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secrete) {
    this.secret = secrete;
  }

  public long getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(long expirationTime) {
    this.expirationTime = expirationTime;
  }
}
