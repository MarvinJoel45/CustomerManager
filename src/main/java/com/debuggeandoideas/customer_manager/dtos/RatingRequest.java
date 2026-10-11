package com.debuggeandoideas.customer_manager.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {

    @NotNull(message = "id restaurant cant be null")
    private UUID idRestaurant;

    @NotNull(message = "id user cant be null")
    private UUID uuidCustomer;

    @Size(min = 10, max = 500, message = "Must contain between 10 and 500 characters")
    private String comment;

    @Min(value = 1, message = "min value 1")
    @Max(value = 5, message = "max value 5")
    private Integer rating;
}
