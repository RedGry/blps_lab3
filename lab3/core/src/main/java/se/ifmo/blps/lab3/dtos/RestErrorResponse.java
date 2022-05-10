package se.ifmo.blps.lab3.dtos;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.time.Instant;
import lombok.Value;

@Value(staticConstructor = "of")
public class RestErrorResponse implements Dto {

  @Serial private static final long serialVersionUID = 90721569605570362L;

  @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSXXX", timezone = "UTC")
  Instant timestamp;

  int status;

  String error;

  String message;

  String requestUrl;
}
