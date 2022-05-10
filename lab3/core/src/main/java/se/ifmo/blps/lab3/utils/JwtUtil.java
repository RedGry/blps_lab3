package se.ifmo.blps.lab3.utils;

import static java.time.temporal.ChronoUnit.HOURS;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import se.ifmo.blps.lab3.configs.properties.JwtProperties;

@Component
@Slf4j
public class JwtUtil {

  private final JwtProperties jwtProperties;
  private final Key secretKey;

  @Autowired
  public JwtUtil(final JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
  }

  public String generateJwtToken(final Authentication authentication) {
    final var username = authentication.getName();

    final var issueDate = Instant.now();
    final var expirationDate = issueDate.plus(jwtProperties.getExpirationHours(), HOURS);

    final String jwtToken =
        Jwts.builder()
            .setSubject(username)
            .claim(
                jwtProperties.getAuthoritiesClaim(),
                authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList())
            .setIssuedAt(Date.from(issueDate))
            .setExpiration(Date.from(expirationDate))
            .signWith(secretKey)
            .compact();

    log.info("Generated JWT token for user={} with expirationDate={}", username, expirationDate);

    return jwtToken;
  }

  public Claims getAllClaimsFromToken(final String jwtToken) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();
  }

  @SuppressWarnings("unchecked")
  public Collection<GrantedAuthority> getAuthorities(final String token) {
    final var claims = getAllClaimsFromToken(token);
    final var authorities = (List<String>) claims.get(jwtProperties.getAuthoritiesClaim());
    return authorities.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toUnmodifiableSet());
  }

  public String getUsername(final String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDate(final String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  public boolean isTokenExpired(final String token) {
    final var expirationTime = getExpirationDate(token);
    return expirationTime.before(new Date());
  }

  public boolean isTokenValid(final String token) {
    return !isTokenExpired(token);
  }
}
