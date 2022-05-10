package se.ifmo.blps.lab3.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.blps.lab3.dtos.AuthorizationDto;
import se.ifmo.blps.lab3.exceptions.UnauthorizedException;
import se.ifmo.blps.lab3.repositories.UserRepository;
import se.ifmo.blps.lab3.utils.JwtUtil;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  @Transactional(readOnly = true)
  public AuthorizationDto authorize(final AuthorizationDto authorizationDto)
      throws UnauthorizedException {
    final var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authorizationDto.getUsername(), authorizationDto.getPassword()));
    if (!authentication.isAuthenticated()) {
      throw new UnauthorizedException("Bad credentials");
    }
    final String token = jwtUtil.generateJwtToken(authentication);
    final var user = userRepository.findByEmail(authentication.getName()).get();
    authorizationDto.setId(user.getId());
    authorizationDto.setToken(token);
    authorizationDto.setRole(user.getRole());
    authorizationDto.setExpiresAt(jwtUtil.getExpirationDate(token).toInstant());
    return authorizationDto;
  }
}
