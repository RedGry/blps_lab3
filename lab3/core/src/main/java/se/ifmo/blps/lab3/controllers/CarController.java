package se.ifmo.blps.lab3.controllers;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.ifmo.blps.lab3.domains.CreditStatus.ACCEPTED;
import static se.ifmo.blps.lab3.domains.CreditStatus.REJECTED;

import java.util.Collections;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import se.ifmo.blps.lab3.domains.User;
import se.ifmo.blps.lab3.dtos.CarDto;
import se.ifmo.blps.lab3.dtos.Status;
import se.ifmo.blps.lab3.dtos.VinDto;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.requests.CarRequestParameters;
import se.ifmo.blps.lab3.services.CarService;
import se.ifmo.blps.lab3.specifications.CarSpecifications;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CarController {

  private final CarService carService;

  @GetMapping(path = "/cars", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public Page<CarDto> getCars(
      final CarRequestParameters carRequestParameters, final Pageable pageable) {
    return carService.getAllDtos(
        CarSpecifications.fromRequestPublic(carRequestParameters), pageable);
  }

  @GetMapping(path = "/cars/{id}", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public CarDto getCarById(final @PathVariable UUID id) throws ResourceNotFoundException {
    final var dto = carService.getDtoById(id);
    return dto.getIsHidden() ? null : dto;
  }

  @GetMapping(path = "/users/{userId}/cars", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "isAuthenticated() && (authentication.principal.id == #userId or hasRole('ROLE_ADMIN'))")
  public Page<CarDto> getCarByOwner(
      final @PathVariable Long userId,
      final CarRequestParameters carRequestParameters,
      final Pageable pageable) {
    return carService.getAllDtosByOwner(
        userId, CarSpecifications.fromRequest(carRequestParameters), pageable);
  }

  @PostMapping(
      path = "users/{userId}/cars",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "isAuthenticated() && (authentication.principal.id == #userId or hasRole('ROLE_ADMIN'))")
  @ResponseStatus(CREATED)
  public CarDto createCar(final @PathVariable Long userId, final @RequestBody @Valid CarDto carDto)
      throws ResourceAlreadyExistsException {
    final var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return carService.createFromDtoWithOwner(user, carDto);
  }

  @PatchMapping(
      path = "users/{userId}/cars/{carId}",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "isAuthenticated() && (authentication.principal.id == #userId or hasRole('ROLE_ADMIN'))")
  public CarDto updateCar(
      final @PathVariable Long userId,
      final @PathVariable UUID carId,
      final @RequestBody @Valid CarDto carDto)
      throws ResourceNotFoundException, IllegalPropertyUpdateException {
    return carService.updateFromDto(carDto, carId);
  }

  @DeleteMapping(path = "users/{userId}/cars/{carId}", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "isAuthenticated() && (authentication.principal.id == #userId or hasRole('ROLE_ADMIN'))")
  @ResponseStatus(NO_CONTENT)
  public void removeCar(final @PathVariable Long userId, final @PathVariable UUID carId)
      throws ResourceNotFoundException {
    carService.removeById(carId);
  }

  @PostMapping(
          path = "/cars/check",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public Status checkVin(
          final @RequestBody @Valid VinDto vinDto) {
    return carService.checkVin(vinDto.getVin());
  }

}
