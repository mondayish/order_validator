package ru.mondayish.order_validator.writers;

import ru.mondayish.order_validator.models.OutputOrder;

import java.util.List;

public interface ConsoleWriter {

    void writeToConsole(List<OutputOrder> orders);
}
