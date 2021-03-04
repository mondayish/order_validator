package ru.mondayish.order_validator.readers;

import org.json.simple.parser.ParseException;
import ru.mondayish.order_validator.models.FileInfo;
import ru.mondayish.order_validator.models.InputOrder;

import java.io.IOException;
import java.util.List;
import static java.util.Map.Entry;

public interface Reader {

    List<Entry<InputOrder, FileInfo>> readFromFile(String path) throws IOException, ParseException;
}
