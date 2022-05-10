package se.ifmo.blps.lab3.services;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.blps.lab3.domains.Brand;
import se.ifmo.blps.lab3.dtos.BrandDto;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.mappers.BrandMapper;
import se.ifmo.blps.lab3.repositories.BrandRepository;

@Service("brandService")
@Transactional(
    rollbackFor = {ResourceNotFoundException.class, ResourceAlreadyExistsException.class})
public class BrandService implements CommonService<Brand, Long, BrandDto> {

  private final BrandRepository brandRepository;
  private final BrandMapper brandMapper;

  @Autowired
  public BrandService(final BrandRepository brandRepository, final BrandMapper brandMapper) {
    this.brandRepository = brandRepository;
    this.brandMapper = brandMapper;
  }

  @Override
  @Transactional
  public BrandDto createFromDto(final BrandDto dto) throws ResourceAlreadyExistsException {
    if (isAlreadyExists(dto)) {
      throw new ResourceAlreadyExistsException(
          format("Brand with name %s already exists.", dto.getName()));
    }
    final var toPersist = brandMapper.mapToPersistable(dto);
    final var persisted = brandRepository.save(toPersist);
    return brandMapper.mapToDto(persisted);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Brand> getAllEntities(final Pageable pageable) {
    return brandRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Brand> getAllEntities(
      final Specification<Brand> specification, final Pageable pageable) {
    return brandRepository.findAll(specification, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BrandDto> getAllDtos(final Pageable pageable) {
    return getAllEntities(pageable).map(brandMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BrandDto> getAllDtos(
      final Specification<Brand> specification, final Pageable pageable) {
    return getAllEntities(specification, pageable).map(brandMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Brand getEntityById(final Long id) throws ResourceNotFoundException {
    return brandRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(format("Brand with id %d was not found.", id)));
  }

  @Override
  @Transactional(readOnly = true)
  public BrandDto getDtoById(final Long id) throws ResourceNotFoundException {
    return brandMapper.mapToDto(getEntityById(id));
  }

  @Override
  @Transactional
  public BrandDto updateFromDto(final BrandDto dto, final Long id)
      throws ResourceNotFoundException, IllegalPropertyUpdateException {
    final var persistable = getEntityById(id);
    brandMapper.updateFromDto(dto, persistable);
    return brandMapper.mapToDto(persistable);
  }

  @Override
  @Transactional
  public void removeById(final Long id) throws ResourceNotFoundException {
    final var persistable = getEntityById(id);
    brandRepository.delete(persistable);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isAlreadyExists(final BrandDto dto) {
    if (dto.getName() == null) {
      return false;
    }
    return brandRepository.existsByName(dto.getName());
  }

  @Transactional(readOnly = true)
  public Page<Brand> getAllEntitiesWithName(final String name, final Pageable pageable) {
    return brandRepository.findAllByName(name, pageable);
  }

  @Transactional(readOnly = true)
  public Page<BrandDto> getAllDtosWithName(final String name, final Pageable pageable) {
    return getAllEntitiesWithName(name, pageable).map(brandMapper::mapToDto);
  }
}
