package com.swansong.familytree.gedcom;

import com.swansong.familytree.utils.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static com.swansong.familytree.gedcom.Individual.*;

public class GedcomWriter {
    private final int MAX_LINE_LENGTH = 255;

    private final Path filePath;

    public GedcomWriter(Path filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) { // just for testing...
        String arg0 = "src/main/resources/gedcom.ged";
        if (args.length == 0) {
            System.out.println("Using default file:" + arg0);
        } else {
            arg0 = args[0];
        }

        List<Individual> individuals = List.of(testIndividual1, testIndividual2, testIndividual3);
        List<Family> families = List.of(Family.testFamily);

        GedcomWriter writer = new GedcomWriter(Paths.get(arg0));
        writer.writeGedcomFile(individuals, families);

    }

    public void writeGedcomFile(List<Individual> individuals, List<Family> families) {
        try {
            writeHeader();
            writeIndividuals(individuals);
            writeFamilies(families);
            writeTrailer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader() throws IOException {
        String header = """
                0 HEAD
                1 SOUR MyGenealogyProgram
                2 VERS 1.0
                2 NAME My Genealogy Program
                2 CORP My Corporation
                3 ADDR 1234 Main Street
                4 CONT Anytown, USA 12345
                1 DEST AnotherGenealogyProgram
                1 DATE 1 JAN 2022
                2 TIME 12:00:00
                1 FILE myGedcomFile.ged
                1 GEDC
                2 VERS 5.5.1
                2 FORM LINEAGE-LINKED
                1 CHAR UTF-8
                1 SUBM @U1@
                0 @U1@ SUBM
                1 NAME Reldon Poulson
                """;
        Files.writeString(filePath, header, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeIndividuals(List<Individual> individuals) throws IOException {
        for (Individual indiv : individuals) {
            String record = String.format("0 @I%d@ INDI\n", indiv.getId());
            record += String.format("1 NAME %s /%s/\n", indiv.getGivenName(), indiv.getSurname());

            record += getDateAndPlace("1 BIRT\n", indiv.getBirthDate(), indiv.getBirthPlace());
            record += getDateAndPlace("1 DEAT\n", indiv.getDeathDate(), indiv.getDeathPlace());
            record += getDateAndPlace("1 CONF\n", indiv.getConfirmationDate(), indiv.getConfirmationPlace());
            record += getDateAndPlace("1 GRAD\n", indiv.getHighSchoolGraduationDate(), indiv.getHighSchoolGraduationPlace());

            record += getIfNotNullOrBlank("1 OCCU %s\n", indiv.getOccupation());
            for (String note : indiv.getNotes()) {
                record += getIfNotNullOrBlank("1 NOTE %s\n", note);
            }

            Files.writeString(filePath, record, StandardOpenOption.APPEND);
        }

    }

    private String getDateAndPlace(String str1, String date, String place) {
        String record = "";
        if (!StringUtils.isNullOrBlank(date) || !StringUtils.isNullOrBlank(place)) {
            record += str1;
            record += getIfNotNullOrBlank("2 DATE %s\n", date);
            record += getIfNotNullOrBlank("2 PLAC %s\n", place);
        }
        return record;
    }

    public String getIfNotNullOrBlank(String tag, String data) {
        if (!StringUtils.isNullOrBlank(data)) {
            if (tag.length() + data.length() <= MAX_LINE_LENGTH) {
                return String.format(tag, data);
            } else {
                throw new RuntimeException("Line too long!!! Max:" + MAX_LINE_LENGTH +
                        " length:" + tag.length() + data.length() +
                        "\n line:" + String.format(tag, data));
            }
        }
        return "";
    }


    private void writeFamilies(List<Family> families) throws IOException {
        for (Family family : families) {
            String record = String.format("0 @F%d@ FAM\n", family.getId());
            if (family.getHusbandId() != null) {
                record += String.format("1 HUSB @I%d@\n", family.getHusbandId());
            }
            if (family.getWifeId() != null) {
                record += String.format("1 WIFE @I%d@\n", family.getWifeId());
            }
            for (Integer childId : family.getChildIds()) {
                record += String.format("1 CHIL @I%d@\n", childId);
            }
            if (family.getMarriageDate() != null && family.getMarriagePlace() != null) {
                record += "1 MARR\n";
                record += String.format("2 DATE %s\n", family.getMarriageDate());
                record += String.format("2 PLAC %s\n", family.getMarriagePlace());
            }
            Files.writeString(filePath, record, StandardOpenOption.APPEND);
        }
    }

    private void writeTrailer() throws IOException {
        String record = "0 TRLR";
        Files.writeString(filePath, record, StandardOpenOption.APPEND);

    }
}