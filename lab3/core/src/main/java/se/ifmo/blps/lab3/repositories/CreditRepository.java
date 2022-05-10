package se.ifmo.blps.lab3.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se.ifmo.blps.lab3.domains.Credit;
import se.ifmo.blps.lab3.domains.CreditStatus;

@Repository("creditRepository")
public interface CreditRepository extends PersistableRepository<Credit, UUID> {

  Page<Credit> findAllByApplicantId(final Long id, final Pageable pageable);

  Page<Credit> findAllByManagerId(final Long id, final Pageable pageable);

  List<Credit> findAllByStatus(final CreditStatus creditStatus);
}
