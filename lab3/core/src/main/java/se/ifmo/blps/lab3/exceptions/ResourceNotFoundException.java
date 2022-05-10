package se.ifmo.blps.lab3.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends Exception {

  @Serial private static final long serialVersionUID = 3839520715789180641L;

  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
