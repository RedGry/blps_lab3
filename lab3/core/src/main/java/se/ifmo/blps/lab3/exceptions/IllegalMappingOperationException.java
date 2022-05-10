package se.ifmo.blps.lab3.exceptions;

import java.io.Serial;

public class IllegalMappingOperationException extends RuntimeException {

  @Serial private static final long serialVersionUID = 4502144543399649502L;

  public IllegalMappingOperationException() {
    super();
  }

  public IllegalMappingOperationException(final String message) {
    super(message);
  }
}
