package se.ifmo.blps.lab3.domains.constraints.validators;

import static java.time.ZoneOffset.UTC;

import java.time.ZonedDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import se.ifmo.blps.lab3.domains.constraints.CurrentYearOrBefore;

public class CurrentYearOrBeforeValidator
    implements ConstraintValidator<CurrentYearOrBefore, Integer> {

  @Override
  public void initialize(final CurrentYearOrBefore constraintAnnotation) {}

  @Override
  public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    final var now = ZonedDateTime.now(UTC);
    final int currentYear = now.getYear();
    return currentYear >= value;
  }
}
