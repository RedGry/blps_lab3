package se.ifmo.blps.lab3.domains;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.domain.Persistable;

@Entity(name = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true)
public class Credit implements Persistable<UUID>, Serializable {

  @Serial private static final long serialVersionUID = 6233229146413899605L;

  @Id @GeneratedValue @NotNull @EqualsAndHashCode.Include private UUID id;

  @Column(nullable = false)
  private Double price;

  @Size(min = 17, max = 17)
  @NotBlank
  @Column(length = 17, nullable = false)
  private String vin;

  @ManyToOne(
      fetch = LAZY,
      cascade = {DETACH, MERGE, REFRESH},
      optional = false)
  @JoinColumn(name = "applicant_id", nullable = false)
  @ToString.Exclude
  private User applicant;

  @ManyToOne(
      fetch = LAZY,
      cascade = {DETACH, MERGE, REFRESH})
  @JoinColumn(name = "manager_id")
  @ToString.Exclude
  private User manager;

  @Enumerated(STRING)
  private CreditStatus status;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }
}
