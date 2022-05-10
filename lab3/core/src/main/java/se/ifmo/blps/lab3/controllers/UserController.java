package se.ifmo.blps.lab3.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.blps.lab3.dtos.UserDto;
import se.ifmo.blps.lab3.services.UserService;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

  private final UserService userService;

  @GetMapping(path = "/cars/{carId}/owner", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("isAuthenticated()")
  public UserDto exposeOwnerData(final @PathVariable UUID carId) {
    return userService.getByOwnedCarId(carId);
  }
}
