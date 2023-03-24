package com.swansong.familytree;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.csvinput.ReadFile;
import com.swansong.familytree.model.PersonBuilder;
import com.swansong.familytree.model.Person;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

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
		ReadFile reader =  new ReadFile();
		ArrayList<Row> csvData = reader.readFile(csvFile);

		Map<String, Person> personMap = new HashMap<>();
		PersonBuilder builder = new PersonBuilder();
		for (Row row : csvData ) {
			personMap.putAll( builder.buildPrimaryPerson(row));
			personMap.putAll( builder.buildFather(row));
			personMap.putAll( builder.buildMother(row));
			personMap.putAll( builder.buildSpouse(row));
			personMap.putAll( builder.buildSpouseFather(row));
			personMap.putAll( builder.buildSpouseMother(row));
			personMap.putAll( builder.buildChildren(row));
		}
		processPersonList(personMap);


	}

	private static void processPersonList(Map<String, Person> personMap) {
		ArrayList<String> keys = new ArrayList<String>(personMap.keySet());
		Collections.sort(keys);
		for(String key: keys) {
			Person person = personMap.get(key);
			System.out.println(key+" "+ person.getKeyAndGenCode());
		}
	}

}
