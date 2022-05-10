package se.ifmo.blps.lab3.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.ifmo.blps.lab3.domains.CreditStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditMetadataDto implements Dto {

  private UUID id;

  private Double price;

  private String applicantEmail;

  private CreditStatus status;
}
