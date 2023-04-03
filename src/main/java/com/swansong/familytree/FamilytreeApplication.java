package com.swansong.familytree;

import com.swansong.familytree.biz.*;
import com.swansong.familytree.csv.Files;
import com.swansong.familytree.csv.ReadFile;
import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Child;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;

import java.io.File;
import java.util.*;

import static java.lang.System.exit;


//@SpringBootApplication
public class FamilytreeApplication {

    public static void main(String[] args) {

//		SpringApplication.run(FamilytreeApplication.class, args);


        List<String> filesToProcess = getFilesToProcess(args);

        Map<String, Person> individualMap = new HashMap<>();
        List<Marriage> marriages = new LinkedList<>();

        for (String fileName : filesToProcess) {
            System.out.println("\nProcessing file:" + fileName);

            ArrayList<Row> csvData = new ReadFile().readFile(fileName);

            // build all the primary people
            for (Row row : csvData) {
                processRow(marriages, row);
            }
            SpousesParentsBuilder.buildSpousesParentsMarriage(marriages, csvData);
            ParentBuilder.buildParents(csvData, marriages, individualMap);

            //ChildBuilder.buildChildren(marriages, individualMap);

        }
        printMarriages(marriages);
        //PersonMap.printIndividualMap();
    }

    private static void processRow(List<Marriage> marriages, Row row) {
        Person mainPerson = PersonBuilder.buildMainPerson(row);
        Person spouse = SpouseBuilder.buildSpouse(row);

        if (spouse != null || Child.buildChildrensNames(row).size() > 0) { // add a marriage if there are children
            Marriage marriage = MarriageBuilder.buildMarriage(mainPerson, spouse, row);
            marriages.add(marriage);
        }

    }

    public static void waitForUser() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Press Enter to continue or Q to quit.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) {
                exit(0);
            } else {
                break;
            }
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


    private static void printMarriages(List<Marriage> marriages) {
        System.out.println("\nMarriages...");
        // build the marriages
        for (Marriage marriage : marriages) {
            String str = String.format("#%-2d %-5s ",
                    marriage.getSourceRow().getNumber(), marriage.getId());
            Person person = marriage.getHusband();
            if (person != null) {
                str += " " + person.toShortString();
            }

            person = marriage.getWife();
            if (person != null) {
                str += " " + person.toShortString();
            }

            str += marriage.childrenToString();
            System.out.println(str);
        }
    }


}
