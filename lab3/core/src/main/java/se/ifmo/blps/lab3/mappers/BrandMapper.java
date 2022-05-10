package se.ifmo.blps.lab3.mappers;

import org.springframework.stereotype.Component;
import se.ifmo.blps.lab3.domains.Brand;
import se.ifmo.blps.lab3.dtos.BrandDto;
import se.ifmo.blps.lab3.exceptions.IllegalMappingOperationException;

@Component("brandMapper")
public class BrandMapper implements Mapper<Brand, BrandDto> {

  @Override
  public Brand mapToPersistable(final BrandDto dto) throws IllegalMappingOperationException {
    throw new IllegalMappingOperationException("Unsupported mapping of BrandDto to Brand.");
  }

  @Override
  public BrandDto mapToDto(final Brand persistable) throws IllegalMappingOperationException {
    return BrandDto.builder().id(persistable.getId()).name(persistable.getName()).build();
  }

  @Override
  public void updateFromDto(final BrandDto dto, final Brand persistable)
      throws IllegalMappingOperationException {
    throw new IllegalMappingOperationException(
        "Unsupported mapping with update of BrandDto to Brand.");
  }
}
