package se.ifmo.blps.lab3.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;
import se.ifmo.blps.lab3.dtos.Dto;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;

interface CommonService<V extends Persistable<K>, K, D extends Dto> {

  D createFromDto(final D dto) throws ResourceAlreadyExistsException;

  Page<V> getAllEntities(final Pageable pageable);

  Page<V> getAllEntities(final Specification<V> specification, final Pageable pageable);

  Page<D> getAllDtos(final Pageable pageable);

  Page<D> getAllDtos(final Specification<V> specification, final Pageable pageable);

  V getEntityById(final K id) throws ResourceNotFoundException;

  D getDtoById(final K id) throws ResourceNotFoundException;

  D updateFromDto(final D dto, final K id)
      throws ResourceNotFoundException, IllegalPropertyUpdateException;

  void removeById(final K id) throws ResourceNotFoundException;

  boolean isAlreadyExists(final D dto);
}
