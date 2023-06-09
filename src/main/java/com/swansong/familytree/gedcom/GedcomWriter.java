package com.swansong.familytree.gedcom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static com.swansong.familytree.gedcom.Individual.*;

@SuppressWarnings("StringConcatenationInLoop")
public class GedcomWriter {
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
                1 FILE %s
                1 CHAR UTF-8
                1 LANG English
                1 DATE 20 APR 2023
                1 GEDC
                2 VERS 5.5.5
                2 FORM LINEAGE-LINKED
                1 SOUR SwansongCustomJavaProgram
                2 VERS 1.0
                1 NOTE Donovan Burdette /Meyer/ compiled the information up until his death in 2001. In 2023, Donovan's grandson, Grant Matthew /Swanson/ converted the records from a custom database using Microsoft Works to a GEDCOM file.
                1 SUBM @U1@
                0 @U1@ SUBM
                1 NAME Grant Matthew /Swanson/
                0 @S1@ SOUR
                1 TITL Information compiled by Donovan Burdette /Meyer/
                1 NOTE Author: Donovan Burdette /Meyer/
                2 CONT Information compiled by Donovan Burdette /Meyer/ up until his death in 2001.
                0 @S2@ SOUR
                1 TITL GEDCOM conversion by Grant Matthew /Swanson/
                1 NOTE Author: Grant Matthew /Swanson/
                2 CONT In 2023, Donovan's grandson, Grant Matthew /Swanson/ converted the records from a custom database using Microsoft Works to a GEDCOM file.
                """;
        Files.writeString(filePath, String.format(header, filePath.getFileName().toString()),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeIndividuals(List<Individual> individuals) throws IOException {
        for (Individual indiv : individuals) {
            String record = String.format("0 @I%d@ INDI\n", indiv.getId());
            record += GedcomUtils.getNameTag(indiv.getGivenName(), indiv.getSurName(), indiv.getSuffix());

            record += GedcomUtils.getIfNotNullOrBlank("2 GIVN %s\n", indiv.getGivenName());
            record += GedcomUtils.getIfNotNullOrBlank("2 SURN %s\n", indiv.getSurName());
            record += GedcomUtils.getIfNotNullOrBlank("2 NICK %s\n", indiv.getNickName());

            for (String altName : indiv.getAliasNames()) {
                record += String.format("1 NAME %s\n", altName + "?");
                record += "2 TYPE aka\n";
                record += GedcomUtils.getIfNotNullOrBlank("2 GIVN %s\n", altName + "?");
            }
            record += GedcomUtils.getIfNotNullOrBlank("1 REFN %s\n", indiv.getReferenceNumber());
            record += GedcomUtils.getIfNotNullOrBlank("1 SEX %s\n", indiv.getGender());

            record += GedcomUtils.getDateAndPlace("1 BIRT\n", indiv.getBirthDate(), indiv.getBirthPlace());
            record += GedcomUtils.getDateAndPlace("1 BAPM\n", indiv.getBaptismDate(), indiv.getBaptismPlace());
            record += GedcomUtils.getDateAndPlace("1 CONF\n", indiv.getConfirmationDate(), indiv.getConfirmationPlace());
            record += GedcomUtils.getDateAndPlace("1 GRAD\n", indiv.getHighSchoolGraduationDate(), indiv.getHighSchoolGraduationPlace());
            record += GedcomUtils.getDateAndPlace("1 DEAT\n", indiv.getDeathDate(), indiv.getDeathPlace());

            record += GedcomUtils.getIfNotNullOrBlank("1 OCCU %s\n", indiv.getOccupation());
            for (String note : indiv.getNotes()) {
                record += GedcomUtils.getIfNotNullOrBlank("1 FACT %s\n", note);
            }
            for (int id : indiv.getMarriageIds()) {
                record += String.format("1 FAMS @F%d@\n", id);
            }
            if (indiv.getParentsMarriageId() != null) {
                record += String.format("1 FAMC @F%d@\n", indiv.getParentsMarriageId());
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
            record += GedcomUtils.getDateAndPlace("1 MARR\n", family.getMarriageDate(), family.getMarriagePlace());
            record += GedcomUtils.getDateAndPlace("1 DIV\n", family.getDivorceDate(), family.getDivorcePlace());

            for (String note : family.getNotes()) {
                record += GedcomUtils.getIfNotNullOrBlank("1 NOTE %s\n", note);
            }

            Files.writeString(filePath, record, StandardOpenOption.APPEND);
        }
    }

    private void writeTrailer() throws IOException {
        String record = "0 TRLR";
        Files.writeString(filePath, record, StandardOpenOption.APPEND);

    }
}