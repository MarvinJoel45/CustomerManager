package com.debuggeandoideas.customer_manager.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    private String restaurantId; //Not null is a UUID format
    private String customerId; //Not null is a UUID format
    private String customerName; //Not null length min 3 max 21
    private String customerEmail; //Not null is email format
    private String dateTime; // valid format: example 2025-06-16,15:30
    private Integer partySize; //Not null
    private String comment;
}
