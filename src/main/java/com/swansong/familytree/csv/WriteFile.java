package com.swansong.familytree.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class WriteFile {
    public static void writeFile(ArrayList<Row> rows, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            Field[] fields = Row.class.getDeclaredFields();
            for (Row row : rows) {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    Object value = field.get(row);
                    writer.append(value != null ? value.toString() : "");
                    if (i < fields.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            writer.flush();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
