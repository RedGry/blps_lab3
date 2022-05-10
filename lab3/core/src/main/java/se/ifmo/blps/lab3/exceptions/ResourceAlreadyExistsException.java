package se.ifmo.blps.lab3.exceptions;

import java.io.Serial;

public class ResourceAlreadyExistsException extends Exception {

  @Serial private static final long serialVersionUID = 4958441245498610922L;

  public ResourceAlreadyExistsException() {
    super();
  }

  public ResourceAlreadyExistsException(final String message) {
    super(message);
  }
}
