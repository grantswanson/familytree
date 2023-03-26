package com.swansong.familytree;

import com.swansong.familytree.csvinput.ReadFile;
import com.swansong.familytree.csvinput.Row;
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
        List <Marriage> marriages = new LinkedList<>();

        // build all the primary people
        for (Row row : csvData) {
            Person mainPerson = PersonBuilder.buildMainPerson(individualMap, row);
            Person spouse = PersonBuilder.buildSpouse(individualMap, row);

            if(spouse!= null) {
                mainPerson.addSpouse(spouse);
                spouse.addSpouse(mainPerson);
                marriages.add(MarriageBuilder.buildMarriage(mainPerson, spouse, row));
            }

        }
        printIndividualMap(individualMap);

        printMarriages( marriages);


//		individualMap.putAll( builder.buildSpouse(row));
//		individualMap.putAll( builder.buildSpouseFather(row));
//		individualMap.putAll( builder.buildSpouseMother(row));
//		individualMap.putAll( builder.buildChildren(row));

    }
    private static void printMarriages(List<Marriage> marriages) {
        System.out.println("\nMarriages...");
        // build the marriages
        for (Marriage marriage: marriages) {
            String str = String.format("#%-2d %-5s %-6s %-30.30s %-6s %-30.30s", marriage.getSourceLineNumber(), marriage.getId(),
                    marriage.getHusband().getGenCode(), marriage.getHusband().getName().getLastCommaFirst(),
                    marriage.getWife().getGenCode(),  marriage.getWife().getName().getLastCommaFirst());
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
            String selfStr = String.format("#%-2d %-6s %-30.30s %-5s",  person.getSourceLineNumber(), person.getGenCode(),
                    person.getName().getLastCommaFirst(), person.getId() );
            System.out.print(selfStr);

            if(person.getSpouses().size()>0) {System.out.print("  spouses:");}
            for (String spouseGenCode : person.getSpouses().keySet()) {
                Person spouse = person.getSpouses().get(spouseGenCode);
                String spouseStr = String.format("#%d %-4s %s", spouse.getSourceLineNumber(), spouseGenCode,
                        spouse.getName().getFirstNames()+", " );
                System.out.print(spouseStr);
            }
            System.out.println();
        }
        System.out.println("Total Count="+(personMap.size()));
    }

}
