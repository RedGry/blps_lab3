package se.ifmo.blps.lab3.dtos;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto implements Dto {

  @Serial private static final long serialVersionUID = 7672682045310392980L;

  @JsonProperty(access = READ_ONLY)
  private Long id;

  @JsonProperty(access = READ_ONLY)
  private String name;
}
