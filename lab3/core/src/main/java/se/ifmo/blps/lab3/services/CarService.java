package se.ifmo.blps.lab3.services;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import se.ifmo.blps.lab3.domains.Car;
import se.ifmo.blps.lab3.domains.User;
import se.ifmo.blps.lab3.dtos.CarDto;
import se.ifmo.blps.lab3.dtos.Status;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.mappers.CarMapper;
import se.ifmo.blps.lab3.repositories.CarRepository;
import se.ifmo.blps.lab3.specifications.CarSpecifications;

@Service("carService")
@Transactional(
    rollbackFor = {ResourceNotFoundException.class, ResourceAlreadyExistsException.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CarService implements CommonService<Car, UUID, CarDto> {

  private final CarRepository carRepository;
  private final CarMapper carMapper;

//  @Autowired
//  private final RestTemplate restTemplate;
//
//  @Value("${vin.decoder.auth.key}")
//  private String authorizationKey;
//
//  @Value("${vin.decoder.partner.token}")
//  private String partnerToken;

  @Override
  @Transactional
  public CarDto createFromDto(final CarDto dto) throws ResourceAlreadyExistsException {
    if (isAlreadyExists(dto)) {
      throw new ResourceAlreadyExistsException(
          format("Car with vin %s already exists.", dto.getVin()));
    }
    final var toPersist = carMapper.mapToPersistable(dto);
    final var persisted = carRepository.save(toPersist);
    return carMapper.mapToDto(persisted);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Car> getAllEntities(final Pageable pageable) {
    return carRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Car> getAllEntities(final Specification<Car> specification, final Pageable pageable) {
    return carRepository.findAll(specification, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CarDto> getAllDtos(final Pageable pageable) {
    return getAllEntities(pageable).map(carMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CarDto> getAllDtos(final Specification<Car> specification, final Pageable pageable) {
    return getAllEntities(specification, pageable).map(carMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Car getEntityById(final UUID id) throws ResourceNotFoundException {
    return carRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(format("Car with id %s was not found.", id)));
  }

  @Override
  @Transactional(readOnly = true)
  public CarDto getDtoById(final UUID id) throws ResourceNotFoundException {
    return carMapper.mapToDto(getEntityById(id));
  }

  @Override
  @Transactional
  public CarDto updateFromDto(final CarDto dto, final UUID id)
      throws ResourceNotFoundException, IllegalPropertyUpdateException {
    final var persistable = getEntityById(id);
    carMapper.updateFromDto(dto, persistable);
    return carMapper.mapToDto(persistable);
  }

  @Override
  @Transactional
  public void removeById(final UUID id) throws ResourceNotFoundException {
    final var persistable = getEntityById(id);
    carRepository.delete(persistable);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isAlreadyExists(final CarDto dto) {
    if (dto.getVin() == null) {
      return false;
    }
    return carRepository.existsByVin(dto.getVin());
  }

  @Transactional
  public CarDto createFromDtoWithOwner(final User owner, final CarDto carDto)
      throws ResourceAlreadyExistsException {
    if (isAlreadyExists(carDto)) {
      throw new ResourceAlreadyExistsException(
          format("Car with vin %s already exists.", carDto.getVin()));
    }
    final var car = carMapper.mapToPersistable(carDto);
    car.setOwner(owner);
    return carMapper.mapToDto(carRepository.save(car));
  }

  @Transactional(readOnly = true)
  public Page<CarDto> getAllDtosByOwner(
      final Long ownerId, final Specification<Car> specification, final Pageable pageable) {
    final Specification<Car> withOwnerSpecification =
        specification.and(CarSpecifications.withOwner(ownerId));
    return carRepository.findAll(withOwnerSpecification, pageable).map(carMapper::mapToDto);
  }

  @Transactional
  public Boolean checkVin(
      final String vin) {

    return carRepository.existsByVin(vin);

//    P.S. API was unavailable in Russia, cringe...
//    RestTemplate restTemplate = new RestTemplate();
//    HttpHeaders headers = new HttpHeaders();
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//    headers.set("authorization", authorizationKey);
//    headers.set("partner-token", partnerToken);
//
//    final HttpEntity<Object> entity = new HttpEntity<>(headers);
//    final var response =
//            restTemplate.exchange("http://api.carmd.com/v3.0/decode?vin=" + vin, GET, entity, Object.class);
//    if (response.getStatusCode() == OK && response.getBody() != null && !response.getBody().toString().contains("data=null")) {
//      log.info("Car with vin " + vin + " is existed: " + response.getBody().toString());
//      return new Status("OK");
//    } else {
//      log.info("Car with vin " + vin + " is not existed");
//      return new Status("FAILED");
//    }
  }
}
