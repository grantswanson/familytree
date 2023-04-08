package com.swansong.familytree;

import com.swansong.familytree.biz.ChildBuilder;
import com.swansong.familytree.biz.ParentBuilder;
import com.swansong.familytree.biz.PersonBuilder;
import com.swansong.familytree.biz.SpousesParentsBuilder;
import com.swansong.familytree.csv.Files;
import com.swansong.familytree.csv.ReadFile;
import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.gedcom.Family;
import com.swansong.familytree.gedcom.GedcomWriter;
import com.swansong.familytree.gedcom.Individual;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.translate.MarriageToFamily;
import com.swansong.familytree.translate.PersonToIndividual;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;


//@SpringBootApplication
public class FamilyTreeETLApplication {
    public static void main(String[] args) {
//		SpringApplication.run(FamilytreeApplication.class, args);

        List<String> filesToProcess = getFilesToProcess(args);

        for (String inputFile : filesToProcess) {
            processInputFile(inputFile);

            MarriageMap.printMarriages();
            PersonMap.printIndividualMap();
            verifyCounts(inputFile);

            List<Individual> individuals = translatePeople();
            List<Family> families = translateMarriages();

            String outputFile = inputFile.replace(".csv", ".ged");
            writeOutputFile(outputFile, individuals, families);
        }


    }

    private static void verifyCounts(String inputFile) {
        int count = PersonMap.count();
        int expectedCount = 129;
        if (count != expectedCount && inputFile.endsWith("SwansonFloyd.csv")) {
            throw new RuntimeException("individual map size is not " + expectedCount + " for SwansonFloyd.csv. size:" + count);
        }
        count = MarriageMap.count();
        expectedCount = 45;
        if (count != expectedCount && inputFile.endsWith("SwansonFloyd.csv")) {
            throw new RuntimeException("Marriage map size is not " + expectedCount + " for SwansonFloyd.csv. size:" + count);
        }
    }

    private static void processInputFile(String fileName) {
        System.out.println("\nProcessing file:" + fileName);
        ArrayList<Row> csvData = new ReadFile().readFile(fileName);

        PersonBuilder.buildMainPersonAndSpouse(csvData);
        SpousesParentsBuilder.buildSpousesParentsMarriage(csvData);
        ParentBuilder.buildParents(csvData);
        ChildBuilder.buildChildren();
    }

    private static List<Individual> translatePeople() {
        List<Individual> individuals = new ArrayList<>();
        for (Person person : PersonMap.getPersonList()) {
            individuals.add(PersonToIndividual.convertPersonToIndividual(person));
        }
        return individuals;
    }

    private static List<Family> translateMarriages() {
        List<Family> families = new ArrayList<>();
        for (Marriage marriage : MarriageMap.getMarriagesCollection()) {
            families.add(MarriageToFamily.convert(marriage));
        }
        return families;
    }

    private static void writeOutputFile(String outputFile, List<Individual> individuals, List<Family> families) {
        GedcomWriter writer = new GedcomWriter(Paths.get(outputFile));
        writer.writeGedcomFile(individuals, families);
    }

    private static void waitForUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press Enter to continue or Q to quit.");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Q")) {
            exit(0);
        }

    }

    private static List<String> getFilesToProcess(String[] args) {
        List<String> filesToProcess;

        String arg0 = "src/main/resources/working";
        if (args.length == 0) {
            System.out.println("Processing default path of:" + arg0);
        } else {
            arg0 = args[0];
        }
        File file = new File(arg0);
        if (file.isFile()) {
            filesToProcess = List.of(new String[]{arg0});
            System.out.println("Processing one file:" + arg0);
        } else if (file.isDirectory()) { // it is a directory
            filesToProcess = Files.findLatestFiles(arg0);
            System.out.println("Processing directory:" + arg0);
            waitForUser();
        } else {
            throw new RuntimeException("File not found:" + arg0);
        }
        return filesToProcess;
    }


}
