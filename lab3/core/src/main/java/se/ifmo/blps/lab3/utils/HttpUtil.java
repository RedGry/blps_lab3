package se.ifmo.blps.lab3.utils;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import se.ifmo.blps.lab3.dtos.RestErrorResponse;

@NoArgsConstructor(access = PRIVATE)
public class HttpUtil {

  public static RestErrorResponse getRestErrorResponse(
      final HttpStatus status,
      final String errorMessage,
      final HttpServletRequest httpServletRequest) {
    return RestErrorResponse.of(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        errorMessage,
        httpServletRequest.getRequestURL().toString());
  }

  public static void writeRestErrorResponse(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Exception exception,
      final HttpStatus httpStatus,
      final ObjectMapper objectMapper)
      throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(httpStatus.value());
    response
        .getWriter()
        .write(
            objectMapper.writeValueAsString(
                RestErrorResponse.of(
                    Instant.now(),
                    httpStatus.value(),
                    httpStatus.getReasonPhrase(),
                    exception.getMessage(),
                    request.getRequestURL().toString())));
  }
}
