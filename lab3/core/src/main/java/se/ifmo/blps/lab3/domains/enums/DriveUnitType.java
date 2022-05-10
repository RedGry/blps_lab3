package se.ifmo.blps.lab3.domains.enums;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
@ToString
public enum DriveUnitType implements Serializable {
  AWD("AWD"),
  FWD("FWD"),
  RWD("RWD");

  @JsonValue
  @JsonProperty("driveUnitType")
  private final String name;

  @NonNull
  @JsonCreator
  public static DriveUnitType forName(final @NonNull String driveUnitType)
      throws IllegalArgumentException {
    for (final var type : values()) {
      if (type.getName().equalsIgnoreCase(driveUnitType.trim())) {
        return type;
      }
    }
    throw new IllegalArgumentException(
        format("Drive Unit Type for name %s was not found.", driveUnitType));
  }
}
