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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;


//@SpringBootApplication
public class FamilytreeApplication {
    public static void main(String[] args) {

//		SpringApplication.run(FamilytreeApplication.class, args);


        List<String> filesToProcess = getFilesToProcess(args);



        for (String fileName : filesToProcess) {
            System.out.println("\nProcessing file:" + fileName);

            ArrayList<Row> csvData = new ReadFile().readFile(fileName);

            PersonBuilder.buildMainPersonAndSpouse(csvData);
            SpousesParentsBuilder.buildSpousesParentsMarriage(csvData);
            ParentBuilder.buildParents(csvData);
            ChildBuilder.buildChildren();

        }
        MarriageMap.printMarriages();
        PersonMap.printIndividualMap();
    }

    public static void waitForUser() {
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
