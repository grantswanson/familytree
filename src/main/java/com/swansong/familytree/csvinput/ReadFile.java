package com.swansong.familytree.csvinput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {
    private static final int MAX_COLUMN_LENGTH = 54;

    public ArrayList<Row> readFile(String csvFile) {
        ArrayList<Row> result = new ArrayList<>();

        String line = "";
        String cvsSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        int maxColLength = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            // read the first line to determine the number of columns
            line = br.readLine();

            while (line != null) {
                // always fill in every column
                String[] finalValues = new String[MAX_COLUMN_LENGTH];

                // use regex to split line by comma, ignoring commas within quotes
                String[] values = line.split(cvsSplitBy);
                maxColLength = Math.max(values.length, maxColLength);
                for (int i = 0; i < values.length; i++) {
                    finalValues[i] = values[i].replaceAll("^\"|\"$", "") // remove extra double quotes
                        .replace("\"\"","").trim(); // removed doubled up double quotes
                }
                Row row = RowBuilder.buildRow(finalValues);
                result.add(row); // add row to result list
                System.out.println(row);

                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Max col Length=" + maxColLength);
        return result;
    }

    class RowBuilder {
        static Row buildRow(String[] values) {

            return Row.builder()
                    .updateDate(values[0])
                    .name(values[1])
                    .genCode(values[2])
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
                    .wife(values[18])
                    .wifeDob(values[19])
                    .wifePob(values[20])
                    .wifeBaptismDate(values[21])
                    .wifeBaptismPlace(values[22])
                    .wifeConfirmationDate(values[23])
                    .wifeConfirmationPlace(values[24])
                    .wifeHsGradDate(values[25])
                    .wifeHsGradPlace(values[26])
                    .wifeDeathDate(values[27])
                    .wifeBurialPlace(values[28])
                    .wifeFather(values[29])
                    .wifeMother(values[30])
                    .wifeOccupation(values[31])
                    .unknown1(values[32])
                    .unknown2(values[33])
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
                    .notes1(values[47])
                    .notes2(values[48])
                    .notes3(values[49])
                    .notes4(values[50])
                    .notes5(values[51])
                    .notes6(values[52])
                    .notes7(values[53])
                    .build();
        }
    }

}