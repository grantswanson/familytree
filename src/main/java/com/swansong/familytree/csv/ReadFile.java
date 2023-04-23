package com.swansong.familytree.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {
    private static final int MAX_COLUMN_LENGTH = 54;

    public ArrayList<Row> readFile(String csvFile) {
        ArrayList<Row> result = new ArrayList<>();

        String cvsSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        int maxColLength = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            // read the first line to determine the number of columns
            String line = br.readLine();
            int lineNumber = 0;

            while (line != null) {
                // always fill in every column
                String[] finalValues = new String[MAX_COLUMN_LENGTH];

                // use regex to split line by comma, ignoring commas within quotes
                String[] values = line.split(cvsSplitBy);
                maxColLength = Math.max(values.length, maxColLength);
                for (int i = 0; i < values.length; i++) {
                    finalValues[i] = values[i].replaceAll("^\"|\"$", "") // remove extra double quotes
                            .replace("\"\"", "\"").trim(); // removed doubled up double quotes
                }
                Row row = RowBuilder.buildRow(finalValues, lineNumber++);
                result.add(row); // add row to result list
                //System.out.println(row);

                line = br.readLine();
            }
            System.out.println("File:" + csvFile + " #rows=" + lineNumber + " #columns=" + maxColLength + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    static class RowBuilder {
        static Row buildRow(String[] values, int i) {

            return Row.builder()
                    .number(i)
                    .updateDate(values[0])
                    .name(values[1])
                    .genCode(values[2].replaceAll(" ", ""))
                    .dob(values[3])
                    .pob(values[4])
                    .baptismDate(values[5])
                    .baptismPlace(values[6])
                    .confirmationDate(values[7])
                    .confirmationPlace(values[8])
                    .hsGradDate(values[9])
                    .hsGradPlace(values[10])
                    .deathDate(values[11])
                    .burialPlace(values[12])
                    .father(values[13])
                    .mother(values[14])
                    .occupation(values[15])
                    .marriageDate(values[16])
                    .marriagePlace(values[17])
                    .spouse(values[18])
                    .spouseDob(values[19])
                    .spousePob(values[20])
                    .spouseBaptismDate(values[21])
                    .spouseBaptismPlace(values[22])
                    .spouseConfirmationDate(values[23])
                    .spouseConfirmationPlace(values[24])
                    .spouseHsGradDate(values[25])
                    .spouseHsGradPlace(values[26])
                    .spouseDeathDate(values[27])
                    .spouseBurialPlace(values[28])
                    .spouseFather(values[29])
                    .spouseMother(values[30])
                    .spouseOccupation(values[31])
                    .divorceDate(values[32])
                    .divorcePlace(values[33])
                    .child1(values[34])
                    .child2(values[35])
                    .child3(values[36])
                    .child4(values[37])
                    .child5(values[38])
                    .child6(values[39])
                    .child7(values[40])
                    .child8(values[41])
                    .child9(values[42])
                    .child10(values[43])
                    .child11(values[44])
                    .child12(values[45])
                    .childrenNotes(values[46])
                    .note1(values[47])
                    .note2(values[48])
                    .note3(values[49])
                    .note4(values[50])
                    .note5(values[51])
                    .note6(values[52])
                    .note7(values[53])
                    .build();
        }
    }

}