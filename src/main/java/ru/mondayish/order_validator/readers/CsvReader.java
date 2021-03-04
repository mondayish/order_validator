package ru.mondayish.order_validator.readers;

import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import ru.mondayish.order_validator.models.Currency;
import ru.mondayish.order_validator.models.FileInfo;
import ru.mondayish.order_validator.models.InputOrder;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

@Component("csvReader")
public class CsvReader implements Reader {

    private static final String OK_RESULT_MESSAGE = "ОК";
    private static final String LINE_ERROR_MESSAGE = "Ошибка в структуре строки, проверьте кол-во колонок";

    private static String getErrorCellMessage(int columnNumber) {
        return "Ошибка в колонке " + columnNumber;
    }

    private static boolean isOkResult(String result) {
        return result.equals(OK_RESULT_MESSAGE);
    }

    @Override
    public List<Entry<InputOrder, FileInfo>> readFromFile(String path) throws IOException {
        try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(path), CsvPreference.STANDARD_PREFERENCE)) {
            CellProcessor[] processors = getProcessors();
            String[] fieldNames = getFieldNames();

            List<Entry<InputOrder, FileInfo>> orders = new ArrayList<>();
            InputOrder inputOrder = InputOrder.EMPTY;
            while (true) {
                String result;
                try {
                    inputOrder = beanReader.read(InputOrder.class, fieldNames, processors);
                    if (inputOrder == null) break;
                    result = OK_RESULT_MESSAGE;
                } catch (SuperCsvCellProcessorException e) {
                    result = getErrorCellMessage(e.getCsvContext().getColumnNumber());
                } catch (IllegalArgumentException e) {
                    result = LINE_ERROR_MESSAGE;
                } catch (Exception e) {
                    result = "jopa";
                }
                orders.add(new SimpleEntry<>(isOkResult(result) ? inputOrder : InputOrder.EMPTY,
                        new FileInfo(beanReader.getLineNumber(), path, result)));
            }
            return orders;
        }
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[]{new Unique(new ParseLong()),
                new NotNull(new ParseDouble()), new ParseEnum(Currency.class), new NotNull()};
    }

    private String[] getFieldNames() {
        return Arrays.stream(InputOrder.class.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName)
                .toArray(String[]::new);
    }
}
