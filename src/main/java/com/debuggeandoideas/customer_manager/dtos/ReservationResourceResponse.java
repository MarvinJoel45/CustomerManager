package com.debuggeandoideas.customer_manager.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResourceResponse {

    @JsonProperty("Resource")
    private String resource;
}
