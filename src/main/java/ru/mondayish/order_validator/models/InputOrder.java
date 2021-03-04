package ru.mondayish.order_validator.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InputOrder {

    public static InputOrder EMPTY = new InputOrder();

    private Long orderId;
    private Double amount;
    private Currency currency;
    private String comment;
}
