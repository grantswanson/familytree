package com.swansong.familytree;

import com.swansong.familytree.csvinput.ReadFile;
import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.biz.MarriageBuilder;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.biz.PersonBuilder;

import java.util.*;
import java.util.stream.Collectors;

//@SpringBootApplication
public class FamilytreeApplication {

    public static void main(String[] args) {

//		SpringApplication.run(FamilytreeApplication.class, args);

        // check that a filename was passed in as an argument
        if (args.length == 0) {
            System.out.println("Please provide a filename as an argument.");
            return;
        }

        String csvFile = args[0];
        ReadFile reader = new ReadFile();
        ArrayList<Row> csvData = reader.readFile(csvFile);

        Map<String, Person> individualMap = new HashMap<>();

        // build all the primary people
        for (Row row : csvData) {
            String selfGenCode =GenCode.buildSelfCode(row.getGenCode());
            Person existingPerson = individualMap.get(selfGenCode);

            if(existingPerson==null) {
                // make new person
                existingPerson = PersonBuilder.buildPrimaryPerson(row);
                individualMap.put(selfGenCode, existingPerson);
            } else {
                existingPerson.appendDebug(" Also indiv ln#:"+row.getNumber());
            }

            Person existingSpouse = individualMap.get(GenCode.buildSpousesCode(row.getGenCode()));
            if(existingSpouse==null) {
                // make new person
                existingSpouse = PersonBuilder.buildSpouse(row);
                if(existingSpouse != null) {
                    individualMap.put(existingSpouse.getGenCode(), existingSpouse);
                }
            } else {
                existingSpouse.appendDebug(" Also spouse ln#:"+row.getNumber());
            }
            if(existingSpouse!= null) {
                existingPerson.addSpouse(existingSpouse);
                existingSpouse.addSpouse(existingPerson);
            }

        }
        processPersonList(individualMap);

//        System.out.println("\nAfter marriages...");
//        List <Marriage> marriages = new LinkedList<>();
//        // build the marriages
//        for (Row row : csvData) {
//            marriages.add(MarriageBuilder.buildMarriage(row, individualMap));
//        }
//        processPersonList(individualMap);

//		individualMap.putAll( builder.buildSpouse(row));
//		individualMap.putAll( builder.buildSpouseFather(row));
//		individualMap.putAll( builder.buildSpouseMother(row));
//		individualMap.putAll( builder.buildChildren(row));

    }

    private static void processPersonList(Map<String, Person> personMap) {
        Comparator<Map.Entry<String, Person>> valueComparator = Comparator
                .comparing((Map.Entry<String, Person> e) -> e.getValue().getSourceLineNumber())
                .thenComparing(e -> e.getValue().getGenCode());
        Map<String, Person> sortedPersonMap = personMap.entrySet()
                .stream()
                .sorted(valueComparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Person> entry : sortedPersonMap.entrySet()) {
            Person person = entry.getValue();
            String selfStr = String.format("#%-2d %-6s %-30.30s",  person.getSourceLineNumber(), person.getGenCode(),
                    person.getName().getLastCommaFirst());
            System.out.print(selfStr);

            if(person.getSpouses().size()>0) {System.out.print("  spouses:");}
            for (String spouseGenCode : person.getSpouses().keySet()) {
                Person spouse = person.getSpouses().get(spouseGenCode);
                String spouseStr = String.format("#%d %-4s %s", spouse.getSourceLineNumber(), spouseGenCode,
                        spouse.getName().getFirstNames()+", " );
                System.out.print(spouseStr);
            }
            System.out.println("");
        }
        System.out.println("Total Count="+(personMap.size()));
    }

}
