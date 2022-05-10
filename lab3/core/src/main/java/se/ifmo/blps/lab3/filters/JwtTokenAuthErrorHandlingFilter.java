package se.ifmo.blps.lab3.filters;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import se.ifmo.blps.lab3.utils.HttpUtil;

@Slf4j
public class JwtTokenAuthErrorHandlingFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  public JwtTokenAuthErrorHandlingFilter() {
    this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
  }

  @Override
  protected void doFilterInternal(
      final @NonNull HttpServletRequest request,
      final @NonNull HttpServletResponse response,
      final @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (final JwtException e) {
      HttpUtil.writeRestErrorResponse(request, response, e, FORBIDDEN, objectMapper);
    } catch (final UsernameNotFoundException e) {
      HttpUtil.writeRestErrorResponse(request, response, e, NOT_FOUND, objectMapper);
    }
  }
}
