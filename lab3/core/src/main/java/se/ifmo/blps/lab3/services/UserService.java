package se.ifmo.blps.lab3.services;

import static java.lang.String.format;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.blps.lab3.domains.User;
import se.ifmo.blps.lab3.dtos.UserDto;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.mappers.UserMapper;
import se.ifmo.blps.lab3.repositories.UserRepository;

@Service("userService")
@Transactional(
    rollbackFor = {ResourceNotFoundException.class, ResourceAlreadyExistsException.class})
public class UserService implements CommonService<User, Long, UserDto>, UserDetailsService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Autowired
  protected UserService(final UserRepository userRepository, final UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  @Transactional
  public UserDto createFromDto(final UserDto dto) throws ResourceAlreadyExistsException {
    if (isAlreadyExists(dto)) {
      throw new ResourceAlreadyExistsException(
          format("USer with email %s already exists.", dto.getEmail()));
    }
    final var toPersist = userMapper.mapToPersistable(dto);
    final var persisted = userRepository.save(toPersist);
    return userMapper.mapToDto(persisted);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> getAllEntities(final Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> getAllEntities(
      final Specification<User> specification, final Pageable pageable) {
    return userRepository.findAll(specification, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> getAllDtos(final Pageable pageable) {
    return getAllEntities(pageable).map(userMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> getAllDtos(
      final Specification<User> specification, final Pageable pageable) {
    return getAllEntities(specification, pageable).map(userMapper::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public User getEntityById(final Long id) throws ResourceNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(format("User with id %s was not found.", id)));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto getDtoById(final Long id) throws ResourceNotFoundException {
    return userMapper.mapToDto(getEntityById(id));
  }

  @Override
  @Transactional
  public UserDto updateFromDto(final UserDto dto, final Long id)
      throws ResourceNotFoundException, IllegalPropertyUpdateException {
    final var persistable = getEntityById(id);
    userMapper.updateFromDto(dto, persistable);
    return userMapper.mapToDto(persistable);
  }

  @Override
  @Transactional
  public void removeById(final Long id) throws ResourceNotFoundException {
    final var persistable = getEntityById(id);
    userRepository.delete(persistable);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isAlreadyExists(UserDto dto) {
    return userRepository.existsByEmail(dto.getEmail());
  }

  @Transactional(readOnly = true)
  public UserDto getByOwnedCarId(final UUID carId) {
    final var user =
        userRepository
            .findByCarId(carId)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        format("Owner for car with id %s was not found.", carId)));
    return userMapper.mapToDto(user);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(username)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    format("Username with email %s was not found.", username)));
  }
}
