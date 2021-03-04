package ru.mondayish.order_validator.writers;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import ru.mondayish.order_validator.models.OutputOrder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonConsoleWriter implements ConsoleWriter {

    @Override
    public void writeToConsole(List<OutputOrder> orders) {
        orders.forEach(order -> {
            Map jsonObject = new LinkedHashMap<>();
            jsonObject.put("id", order.getId());
            jsonObject.put("amount", order.getAmount());
            jsonObject.put("currency", order.getCurrency());
            jsonObject.put("comment", order.getComment());
            jsonObject.put("filename", order.getFilename());
            jsonObject.put("line", order.getLine());
            jsonObject.put("result", order.getResult());
            System.out.println(JSONObject.toJSONString(jsonObject));
        });
    }
}
