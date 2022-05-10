package se.ifmo.blps.lab3.mappers;

import org.springframework.stereotype.Component;
import se.ifmo.blps.lab3.domains.Car;
import se.ifmo.blps.lab3.dtos.CarDto;
import se.ifmo.blps.lab3.exceptions.IllegalMappingOperationException;

@Component("carMapper")
public class CarMapper implements Mapper<Car, CarDto> {

  @Override
  public Car mapToPersistable(final CarDto dto) throws IllegalMappingOperationException {
    return Car.builder()
        .vin(dto.getVin())
        .brandId(dto.getBrandId())
        .modelName(dto.getModelName())
        .year(dto.getYear())
        .city(dto.getCity())
        .price(dto.getPrice())
        .mileage(dto.getMileage())
        .generation(dto.getGeneration())
        .driveUnitType(dto.getDriveUnitType())
        .fuelType(dto.getFuelType())
        .gearboxType(dto.getGearboxType())
        .color(dto.getColor())
        .additionalInfo(dto.getAdditionalInfo())
        .build();
  }

  @Override
  public CarDto mapToDto(final Car persistable) throws IllegalMappingOperationException {
    return CarDto.builder()
        .id(persistable.getId())
        .vin(persistable.getVin())
        .brandId(persistable.getBrandId())
        .modelName(persistable.getModelName())
        .year(persistable.getYear())
        .city(persistable.getCity())
        .price(persistable.getPrice())
        .mileage(persistable.getMileage())
        .generation(persistable.getGeneration())
        .driveUnitType(persistable.getDriveUnitType())
        .fuelType(persistable.getFuelType())
        .gearboxType(persistable.getGearboxType())
        .color(persistable.getColor())
        .additionalInfo(persistable.getAdditionalInfo())
        .isSold(persistable.isSold())
        .isHidden(persistable.isHidden())
        .build();
  }

  @Override
  public void updateFromDto(final CarDto dto, final Car car)
      throws IllegalMappingOperationException {
    if (dto.getVin() != null) {
      car.setVin(dto.getVin());
    }
    if (dto.getBrandId() != null) {
      car.setBrandId(dto.getBrandId());
    }
    if (dto.getModelName() != null) {
      car.setModelName(dto.getModelName());
    }
    if (dto.getYear() != null) {
      car.setYear(dto.getYear());
    }
    if (dto.getCity() != null) {
      car.setCity(dto.getCity());
    }
    if (dto.getPrice() != null) {
      car.setPrice(dto.getPrice());
    }
    if (dto.getMileage() != null) {
      car.setMileage(dto.getMileage());
    }
    if (dto.getGeneration() != null) {
      car.setGeneration(dto.getGeneration());
    }
    if (dto.getDriveUnitType() != null) {
      car.setDriveUnitType(dto.getDriveUnitType());
    }
    if (dto.getGearboxType() != null) {
      car.setGearboxType(dto.getGearboxType());
    }
    if (dto.getColor() != null) {
      car.setColor(dto.getColor());
    }
    if (dto.getAdditionalInfo() != null) {
      car.setAdditionalInfo(dto.getAdditionalInfo());
    }
    if (dto.getIsSold() != null) {
      car.setSold(dto.getIsSold());
    }
    if (dto.getIsHidden() != null) {
      car.setHidden(dto.getIsHidden());
    }
  }
}
