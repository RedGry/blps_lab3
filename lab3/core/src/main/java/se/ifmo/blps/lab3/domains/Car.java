package se.ifmo.blps.lab3.domains;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
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
import se.ifmo.blps.lab3.domains.constraints.CurrentYearOrBefore;
import se.ifmo.blps.lab3.domains.converters.DriveUnitTypeConverter;
import se.ifmo.blps.lab3.domains.converters.FuelTypeConverter;
import se.ifmo.blps.lab3.domains.converters.GearboxTypeConverter;
import se.ifmo.blps.lab3.domains.enums.DriveUnitType;
import se.ifmo.blps.lab3.domains.enums.FuelType;
import se.ifmo.blps.lab3.domains.enums.GearboxType;

@Entity(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true)
public class Car implements Persistable<UUID>, Serializable {

  @Serial private static final long serialVersionUID = 6233229146413899605L;

  @Id @GeneratedValue @NotNull @EqualsAndHashCode.Include private UUID id;

  @NaturalId(mutable = true)
  @Size(min = 17, max = 17)
  @NotBlank
  @Column(length = 17, nullable = false, unique = true)
  private String vin;

  @ManyToOne(
      fetch = LAZY,
      cascade = {DETACH, MERGE, REFRESH},
      optional = false)
  @JoinColumn(name = "brand_id")
  @ToString.Exclude
  private Brand brand;

  @Transient private Long brandId;

  @Size(max = 128)
  @NotBlank
  @Column(name = "model_name", length = 128, nullable = false)
  private String modelName;

  @CurrentYearOrBefore
  @Column(nullable = false)
  private Integer year;

  @Size(max = 128)
  @NotBlank
  @Column(length = 128, nullable = false)
  private String city;

  @DecimalMin("1")
  @Column(nullable = false)
  private Double price;

  @DecimalMin("0")
  @Column(nullable = false)
  private Double mileage;

  @Min(1)
  @Column(nullable = false)
  private Integer generation;

  @NotNull
  @Convert(converter = DriveUnitTypeConverter.class)
  @Column(name = "drive_unit_type", nullable = false)
  private DriveUnitType driveUnitType;

  @NotNull
  @Convert(converter = FuelTypeConverter.class)
  @Column(name = "fuel_type", nullable = false)
  private FuelType fuelType;

  @NotNull
  @Convert(converter = GearboxTypeConverter.class)
  @Column(name = "gearbox_type", nullable = false)
  private GearboxType gearboxType;

  @Size(max = 128)
  @NotBlank
  @Column(length = 128, nullable = false)
  private String color;

  @Size(min = 16, max = 8192)
  @NotBlank
  @Column(name = "additional_info", length = 8192, nullable = false)
  private String additionalInfo;

  @Column(name = "is_sold", nullable = false)
  @Builder.Default
  private boolean isSold = false;

  @Column(name = "is_hidden", nullable = false)
  @Builder.Default
  private boolean isHidden = false;

  @ManyToOne(
      fetch = LAZY,
      cascade = {DETACH, MERGE, REFRESH},
      optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  @ToString.Exclude
  private User owner;

  @PostLoad
  public void initBrandId() {
    if (brand == null) {
      return;
    }
    brandId = brand.getId();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }
}
