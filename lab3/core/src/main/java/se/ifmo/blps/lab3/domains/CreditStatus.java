package se.ifmo.blps.lab3.domains;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CreditStatus {
  CREATED,
  ACCEPTED,
  REJECTED,
  REVIEW;

  @JsonCreator
  public static CreditStatus forValue(final String value) {
    for (final var status : values()) {
      if (status.toString().equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw new IllegalArgumentException(format("No status with text %s found.", value));
  }
}
