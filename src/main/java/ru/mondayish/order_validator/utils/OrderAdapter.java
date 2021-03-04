package ru.mondayish.order_validator.utils;

import ru.mondayish.order_validator.models.FileInfo;
import ru.mondayish.order_validator.models.InputOrder;
import ru.mondayish.order_validator.models.OutputOrder;

import java.util.Optional;

public class OrderAdapter {

    public static OutputOrder convertToOutputOrder(InputOrder inputOrder, FileInfo fileInfo) {
        OutputOrder outputOrder = new OutputOrder();
        outputOrder.setId(inputOrder.getOrderId());
        outputOrder.setAmount(inputOrder.getAmount());
        outputOrder.setCurrency(inputOrder.getCurrency());
        outputOrder.setComment(inputOrder.getComment());
        outputOrder.setFilename(fileInfo.getFilename());
        outputOrder.setLine(fileInfo.getLine());
        outputOrder.setResult(fileInfo.getResult());
        return outputOrder;
    }
}
