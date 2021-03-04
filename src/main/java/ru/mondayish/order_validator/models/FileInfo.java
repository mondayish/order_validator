package ru.mondayish.order_validator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FileInfo {

    private long line;
    private String filename;
    private String result;
}
