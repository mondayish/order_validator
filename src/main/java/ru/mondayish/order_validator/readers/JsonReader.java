package ru.mondayish.order_validator.readers;

import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mondayish.order_validator.models.Currency;
import ru.mondayish.order_validator.models.FileInfo;
import ru.mondayish.order_validator.models.InputOrder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Map.Entry;
import static java.util.AbstractMap.SimpleEntry;

@Component("jsonReader")
public class JsonReader implements Reader {

    private static final String OK_RESULT_MESSAGE = "ОК";

    private static String getErrorCellMessage(String fieldName) {
        return "Ошибка в поле " + fieldName;
    }

    @Override
    public List<Entry<InputOrder, FileInfo>> readFromFile(String path) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(path));
        List<Entry<InputOrder, FileInfo>> orders = new ArrayList<>();
        int index = 0;
        for (Object ob : jsonArray) {
            Entry<InputOrder, String> validationResult = getValidationResult((JSONObject) ob);
            orders.add(new SimpleEntry<>(validationResult.getKey(), new FileInfo(index++, path, validationResult.getValue())));
        }
        return orders;
    }

    private Entry<InputOrder, String> getValidationResult(JSONObject jsonObject) {
        try {
            InputOrder inputOrder = new InputOrder();
            Long orderId = parseLong(jsonObject.get("orderId"));
            if (orderId == null) return new SimpleEntry<>(InputOrder.EMPTY, getErrorCellMessage("orderId"));
            inputOrder.setOrderId(orderId);

            Double amount = parseDouble(jsonObject.get("amount"));
            if (amount == null) return new SimpleEntry<>(InputOrder.EMPTY, getErrorCellMessage("amount"));
            inputOrder.setAmount(amount);

            Currency currency = Currency.valueOf((String) jsonObject.get("currency"));
            inputOrder.setCurrency(currency);

            Object comment = jsonObject.get("comment");
            if (!(comment instanceof String))
                return new SimpleEntry<>(InputOrder.EMPTY, getErrorCellMessage("comment"));
            inputOrder.setComment((String) comment);
            return new SimpleEntry<>(inputOrder, OK_RESULT_MESSAGE);
        } catch (IllegalArgumentException | ClassCastException e) {
            return new SimpleEntry<>(InputOrder.EMPTY, getErrorCellMessage("currency"));
        }
    }

    private Long parseLong(Object ob) {
        try {
            return ((Number) ob).longValue();
        } catch (ClassCastException | NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(Object ob) {
        try {
            return ((Number) ob).doubleValue();
        } catch (ClassCastException | NumberFormatException e) {
            return null;
        }
    }
}
