package ru.mondayish.order_validator;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mondayish.order_validator.models.FileInfo;
import ru.mondayish.order_validator.models.InputOrder;
import ru.mondayish.order_validator.models.OutputOrder;
import ru.mondayish.order_validator.readers.Reader;
import ru.mondayish.order_validator.utils.OrderAdapter;
import ru.mondayish.order_validator.writers.ConsoleWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

@Component
public class OrderValidatorCommandLineRunner implements CommandLineRunner {

    private final Reader jsonReader;
    private final Reader csvReader;
    private final ConsoleWriter writer;

    public OrderValidatorCommandLineRunner(@Qualifier("jsonReader") Reader jsonReader, @Qualifier("csvReader") Reader csvReader, ConsoleWriter writer) {
        this.jsonReader = jsonReader;
        this.csvReader = csvReader;
        this.writer = writer;
    }

    @Override
    public void run(String... args) {
        List<Entry<InputOrder, FileInfo>> allEntries = new ArrayList<>();
        for (String fileName : args) {
            try {
                if (fileName.endsWith(".csv")) allEntries.addAll(csvReader.readFromFile(fileName));
                else if (fileName.endsWith(".json")) allEntries.addAll(jsonReader.readFromFile(fileName));
            } catch (IOException | ParseException e) {
                System.err.println("Problems with file " + fileName);
            }
        }
        List<OutputOrder> outputOrders = allEntries.stream()
                .map(entry -> OrderAdapter.convertToOutputOrder(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        writer.writeToConsole(outputOrders);
    }
}
