package se.ifmo.blps.lab3.domains.converters;

import javax.persistence.AttributeConverter;
import org.springframework.lang.Nullable;
import se.ifmo.blps.lab3.domains.enums.FuelType;

public class FuelTypeConverter implements AttributeConverter<FuelType, String> {

  @Nullable
  @Override
  public String convertToDatabaseColumn(final FuelType attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getName();
  }

  @Nullable
  @Override
  public FuelType convertToEntityAttribute(final String dbData) {
    if (dbData == null) {
      return null;
    }

    return FuelType.forName(dbData);
  }
}
