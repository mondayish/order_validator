package ru.mondayish.order_validator.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputOrder {

    private Long id;
    private Double amount;
    private Currency currency;
    private String comment;
    private String filename;
    private long line;
    private String result;
}
