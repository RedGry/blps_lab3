package se.ifmo.blps.lab3.dtos;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.ifmo.blps.lab3.domains.CreditStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDto implements Dto {

  @JsonProperty(access = READ_ONLY)
  private UUID id;

  @Size(min = 17, max = 17)
  private String vin;

  @DecimalMin("1")
  private Double price;

  @JsonProperty(access = READ_ONLY)
  private Long applicantId;

  @JsonProperty(access = READ_ONLY)
  private String applicantEmail;

  @JsonProperty(access = READ_ONLY)
  private Long managerId;

  @JsonProperty(access = READ_ONLY)
  private String managerEmail;

  private CreditStatus status;
}
