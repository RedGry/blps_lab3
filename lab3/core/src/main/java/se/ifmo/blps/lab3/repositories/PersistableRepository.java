package se.ifmo.blps.lab3.repositories;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersistableRepository<T extends Persistable<ID>, ID>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {}
