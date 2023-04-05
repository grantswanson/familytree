package com.swansong.familytree.gedcom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static com.swansong.familytree.gedcom.Individual.*;

public class GedcomWriter {
    private final Path filePath;

    public GedcomWriter(Path filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        String arg0 = "src/main/resources/gedcom.ged";
        if (args.length == 0) {
            System.out.println("Using default file:" + arg0);
        } else {
            arg0 = args[0];
        }
        GedcomWriter writer = new GedcomWriter(Paths.get(arg0));

        List<Individual> individuals = List.of(testIndividual1, testIndividual2, testIndividual3);
        List<Family> families = List.of(Family.testFamily);
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
        for (Individual individual : individuals) {
            String record = String.format("0 @I%d@ INDI\n", individual.getId());
            record += String.format("1 NAME %s /%s/\n", individual.getGivenName(), individual.getSurname());
            if (individual.getBirthDate() != null && individual.getBirthPlace() != null) {
                record += "1 BIRT\n";
                record += String.format("2 DATE %s\n", individual.getBirthDate());
                record += String.format("2 PLAC %s\n", individual.getBirthPlace());
            }
            if (individual.getDeathDate() != null && individual.getDeathPlace() != null) {
                record += "1 DEAT\n";
                record += String.format("2 DATE %s\n", individual.getDeathDate());
                record += String.format("2 PLAC %s\n", individual.getDeathPlace());
            }
            if (individual.getConfirmationDate() != null && individual.getConfirmationPlace() != null) {
                record += "1 CONF\n";
                record += String.format("2 DATE %s\n", individual.getConfirmationDate());
                record += String.format("2 PLAC %s\n", individual.getConfirmationPlace());
            }
            if (individual.getHighSchoolGraduationDate() != null && individual.getHighSchoolGraduationPlace() != null) {
                record += "1 GRAD\n";
                record += String.format("2 DATE %s\n", individual.getHighSchoolGraduationDate());
                record += String.format("2 PLAC %s\n", individual.getHighSchoolGraduationPlace());
            }
            if (individual.getOccupation() != null) {
                record += String.format("1 OCCU %s\n", individual.getOccupation());
            }
            if (individual.getNote() != null) {
                record += String.format("1 NOTE %s\n", individual.getNote());
            }
            Files.writeString(filePath, record, StandardOpenOption.APPEND);
        }
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