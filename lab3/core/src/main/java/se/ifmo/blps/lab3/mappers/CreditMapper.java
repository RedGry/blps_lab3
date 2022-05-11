package se.ifmo.blps.lab3.mappers;

import org.springframework.stereotype.Component;
import se.ifmo.blps.lab3.domains.Credit;
import se.ifmo.blps.lab3.dtos.CreditDto;
import se.ifmo.blps.lab3.exceptions.IllegalMappingOperationException;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;

@Component("creditMapper")
public class CreditMapper implements Mapper<Credit, CreditDto> {
  @Override
  public Credit mapToPersistable(CreditDto dto) throws IllegalMappingOperationException {
    return Credit.builder()
            .price(dto.getPrice())
            .vin(dto.getVin())
            .build();
  }

  @Override
  public CreditDto mapToDto(Credit persistable) throws IllegalMappingOperationException {
    return CreditDto.builder()
        .id(persistable.getId())
        .price(persistable.getPrice())
        .vin(persistable.getVin())
        .applicantId(persistable.getApplicant() != null ? persistable.getApplicant().getId() : null)
        .applicantEmail(
            persistable.getApplicant() != null ? persistable.getApplicant().getEmail() : null)
        .managerId(persistable.getManager() != null ? persistable.getManager().getId() : null)
        .managerEmail(persistable.getManager() != null ? persistable.getManager().getEmail() : null)
        .status(persistable.getStatus())
        .build();
  }

  @Override
  public void updateFromDto(CreditDto dto, Credit persistable)
      throws IllegalMappingOperationException, IllegalPropertyUpdateException {
    if (dto.getPrice() != null) {
      persistable.setPrice(dto.getPrice());
    }
  }
}
