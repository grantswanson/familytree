package com.swansong.familytree.nontest;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CouplesSorter {

    public static void main(String[] args) throws IOException {
        Set<List<String>> uniqueCouples = new HashSet<>();

        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/couplesTest.csv"))) {
            List<String[]> lines = reader.readAll();

            for (String[] couple : lines) {
                if (couple.length < 2) {
                    System.err.println("Skipping row with fewer than 2 columns: " + Arrays.toString(couple));
                    continue;
                }

                List<String> sortedCouple = Arrays.asList(couple[0].trim(), couple[1].trim());
                Collections.sort(sortedCouple);
                uniqueCouples.add(sortedCouple);
            }
        } catch (IOException | CsvException e) {
            System.err.println("An error occurred while reading the CSV file: " + e.getMessage());
        }

        for (List<String> couple : uniqueCouples) {
            System.out.println(couple.get(0) + "  " + couple.get(1));
        }
    }
}