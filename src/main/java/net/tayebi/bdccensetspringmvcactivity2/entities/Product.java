package net.tayebi.bdccensetspringmvcactivity2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Product {
    @Id  @GeneratedValue
    private Long id;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String name;
    @Min(1)
    private double price;
    private double quantity;
}
