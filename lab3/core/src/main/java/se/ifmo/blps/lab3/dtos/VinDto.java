package se.ifmo.blps.lab3.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VinDto {

    @Size(min = 17, max = 17)
    private String vin;

}
