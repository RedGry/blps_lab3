package se.ifmo.blps.lab3.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.blps.lab3.domains.enums.DriveUnitType;
import se.ifmo.blps.lab3.domains.enums.FuelType;
import se.ifmo.blps.lab3.domains.enums.GearboxType;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EnumsController {

  @GetMapping(path = "/drive-unit-types", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public List<DriveUnitType> getDriveUnitTypes() {
    return Arrays.asList(DriveUnitType.values());
  }

  @GetMapping(path = "/fuel-types", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public List<FuelType> getFuelTypes() {
    return Arrays.asList(FuelType.values());
  }

  @GetMapping(path = "/gearbox-types", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public List<GearboxType> getGearboxTypes() {
    return Arrays.asList(GearboxType.values());
  }
}
