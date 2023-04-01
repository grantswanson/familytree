package com.swansong.familytree;

import com.swansong.familytree.biz.*;
import com.swansong.familytree.csv.Files;
import com.swansong.familytree.csv.ReadFile;
import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Child;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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
                processRow(individualMap, marriages, row);
            }

            for (Row row : csvData) {
                ParentBuilder.mergeInParentsAndChildren(row, individualMap);
            }
            for (Row row : csvData) {
                ChildBuilder.mergeInChildren(row, individualMap);
            }
        }
        printMarriages(marriages);
        printIndividualMap(individualMap);
    }

    private static void processRow(Map<String, Person> individualMap, List<Marriage> marriages, Row row) {
        Person mainPerson = PersonBuilder.buildMainPerson(individualMap, row);
        Person spouse = SpouseBuilder.buildSpouse(individualMap, row);
        List<Name> chidrensNames = Child.buildChildrensNames(row);

        if (spouse != null || chidrensNames.size() > 0) {
            Marriage marriage = MarriageBuilder.buildMarriage(mainPerson, spouse, chidrensNames, row);
            marriages.add(marriage);
        }

        Marriage spousesParentsMarriage = SpousesParentsBuilder.buildSpousesParentsMarriage(individualMap, row, spouse);
        if (spousesParentsMarriage != null) {
            marriages.add(spousesParentsMarriage);
        }

        if (row.getFather() != null && !row.getFather().isBlank()) {
            mainPerson.setFathersName(Name.parseLastCommaFirstName(row.getFather()));
        }

        if (row.getMother() != null && !row.getMother().isBlank()) {
            mainPerson.setMothersName(Name.parseLastCommaFirstName(row.getMother()));
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
                    marriage.getSourceLineNumber(), marriage.getId());
            Person person = marriage.getHusband();
            if (person != null) {
                str += " " + person.toShortString();
            }

            person = marriage.getWife();
            if (person != null) {
                str += " " + person.toShortString();
            }

            str += marriage.childrenToString();
            str += marriage.childrensNamesToString();
            System.out.println(str);
        }
    }


    private static void printIndividualMap(Map<String, Person> personMap) {
        Comparator<Map.Entry<String, Person>> valueComparator = Comparator
                .comparing((Map.Entry<String, Person> e) -> e.getValue().getSourceLineNumber())
                .thenComparing(e -> e.getValue().getGenCode());
        Map<String, Person> sortedPersonMap = personMap.entrySet()
                .stream()
                .sorted(valueComparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Person> entry : sortedPersonMap.entrySet()) {
            Person person = entry.getValue();
            String selfStr = String.format("#%-2d %-7s %1s %-30.30s",
                    person.getSourceLineNumber(),
                    person.getGenCode(),
                    person.getGender(),
                    person.getName().getLastCommaFirst()
            );

//            selfStr += String.format("%-11s %-15.15s %-11s %-15.15s %s %s %s %s %s %s ",
//
//                    person.getDob(),
//                    person.getPob(),
//                    person.getBaptismDate(),
//                    person.getBaptismPlace(),
//                    person.getConfirmationDate(),
//                    person.getConfirmationPlace(),
//                    person.getDeathDate(),
//                    person.getDeathPlace(),
//                    person.getBurialDate(),
//                    person.getBurialPlace()
//                    );
//            System.out.println(person);
            System.out.print(selfStr);
            System.out.print(person.spousesToString());
            System.out.print(person.parentsToString());

            System.out.println();
        }

        System.out.println("Total Count=" + (personMap.size()));
    }

}
