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
public enum GearboxType implements Serializable {
  MANUAL("Manual"),
  AUTO("Auto"),
  ROBOTIC("Robotic");

  @JsonValue
  @JsonProperty("gearboxType")
  private final String name;

  @NonNull
  @JsonCreator
  public static GearboxType forName(final @NonNull String gearboxType)
      throws IllegalArgumentException {
    for (final var type : values()) {
      if (type.getName().equalsIgnoreCase(gearboxType.trim())) {
        return type;
      }
    }
    throw new IllegalArgumentException(
        format("Gearbox Type for name %s was not found.", gearboxType));
  }
}
