package se.ifmo.blps.lab3.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditNotificationDto implements Dto {

  private String email;

  private List<CreditMetadataDto> credits;
}
