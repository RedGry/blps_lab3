package se.ifmo.blps.lab3.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.blps.lab3.dtos.BrandDto;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.services.BrandService;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrandController {

  private final BrandService brandService;

  @GetMapping(path = "/brands", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public Page<BrandDto> getBrands(
      final @RequestParam(required = false) String name, final Pageable pageable) {
    if (name == null) {
      return brandService.getAllDtos(pageable);
    }
    return brandService.getAllDtosWithName(name, pageable);
  }

  @GetMapping(path = "/brands/{id}", produces = APPLICATION_JSON_VALUE)
  @PreAuthorize("permitAll()")
  public BrandDto getBrandById(final @PathVariable Long id) throws ResourceNotFoundException {
    return brandService.getDtoById(id);
  }
}
