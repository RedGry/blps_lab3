package se.ifmo.blps.lab3.dtos;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.ifmo.blps.lab3.domains.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Dto {

  @Serial private static final long serialVersionUID = 8870747020716633775L;

  @JsonProperty(access = READ_ONLY)
  private String email;

  @JsonProperty(access = READ_ONLY)
  private String phoneNumber;

  @JsonProperty(access = READ_ONLY)
  private Role role;
}
