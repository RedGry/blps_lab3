package se.ifmo.blps.lab3.domains;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
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

@Entity(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true)
public class Brand implements Persistable<Long>, Serializable {

  @Serial private static final long serialVersionUID = -5005727408430635551L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NaturalId(mutable = true)
  @Size(max = 128)
  @NotBlank
  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "brand", fetch = LAZY, cascade = ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private List<Car> cars = new ArrayList<>();

  public Brand(final String name) {
    this.name = name;
    this.cars = new ArrayList<>();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }

  public void addCar(final Car car) {
    if (cars.contains(car)) {
      return;
    }
    cars.add(car);
    car.setBrand(this);
  }

  public void addCars(final Iterable<Car> cars) {
    for (final var car : cars) {
      addCar(car);
    }
  }
}
