package com.swansong.familytree.model;

import com.swansong.familytree.csv.Row;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Person {
    private final static String defaultSource = "Donovan Burdette Meyer";
    private final static String MALE = "M"; // unknown is blank
    private final static String FEMALE = "F"; // unknown is blank

    public Person() {
        id = Id.PERSON.nextId();
    }

    private int id;
    private String genCode;
    private Name name;
    private String gender = "";

    private Person father;
    private Person mother;
    private String dob = ""; //format is yyyy MMM d(for MOST dates, some are just yyyy)
    private String pob = "";
    private String baptismDate = ""; //format yyyy or yyyy MMM d
    private String baptismPlace = "";
    private String confirmationDate = "";
    private String confirmationPlace = "";
    private String highSchoolGradDate = "";
    private String highSchoolGradPlace = "";
    private String deathDate = "";
    private String burialPlace = "";
    private String occupation = "";
    private List<String> notes = new ArrayList<>();

    private String childrenNotes = "";
    private String debug = "";

    private String source = defaultSource;
    private Row sourceRow;

    @ToString.Exclude
    private Map<String, Person> spouses = new HashMap<>();

    @ToString.Exclude
    List<Marriage> marriages = new ArrayList<>();

    public boolean hasChildRelatedNotes() {
        return (childrenNotes != null && !childrenNotes.equals("")) ||
                name.isAsteriskPresent() || GenCode.isUnrelated(genCode);
    }


    public void addNote(String note) {
        notes.add(note.trim());
        System.out.println("Added note to:" + name.toNameKey().toString() + " note:" + note.trim());
    }

    public void addSpouse(Person spouse) {
        if (spouse == null) {
            return;
        }
        spouses.put(spouse.getGenCode(), spouse);
    }

    @ToString.Include
    public String spousesToString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse = entry.getValue();
            strBuilder.append(String.format("#%d %-4s %-14s ", spouse.getSourceRow().getNumber(), spouse.getGenCode(),
                    spouse.getName().getFirstNames() + ", "));
        }
        String str = strBuilder.toString();
        if (!str.isEmpty()) {
            str = " spouses:" + str;
        }
        return str;
    }

    public void setSpousesGender(boolean isMale) {
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse = entry.getValue();
            spouse.setGenderToMale(isMale);
            spouse.setSpousesGenderNonRecursive(!isMale);
        }
    }

    private void setSpousesGenderNonRecursive(boolean isMale) {
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse = entry.getValue();
            spouse.setGenderToMale(isMale);
        }
    }


    public void setGenderToMale(boolean isMale) {
        if (isMale) {
            gender = MALE;
        } else {
            gender = FEMALE;
        }
    }

    public boolean isMale() {
        return MALE.equals(gender);
    }

    @SuppressWarnings("unused")
    public boolean isFemale() {
        return FEMALE.equals(gender);
    }


    public void appendDebug(String s) {
        debug += s;
    }


    @SuppressWarnings("unused")
    public Marriage getMarriage(int i) {
        return marriages.get(i);
    }

    public void addMarriage(Marriage marriage) {
        marriages.add(marriage);
    }


    public String parentsToString() {
        String str = "";
        if (father != null) {
            str = " Dad:" + father.getGenCode() + " " + father.getName().toFullName();
        }
        if (mother != null) {
            str += " Mom:" + mother.getGenCode() + " " + mother.getName().toFullName();
        }

        return str;
    }

    public String toShortString() {
        return String.format("#%d %-5s%s %-1s %-30.30s",
                sourceRow.getNumber(), genCode, (hasChildRelatedNotes() ? "*" : ""), gender,
                name.toFullName());
    }

    public String toFormattedString() {
        String str = String.format("#%-2d %-7s %1s %-30.30s",
                getSourceRow().getNumber(),
                getGenCode(),
                getGender(),
                getName().toFullName()
        );

        str += String.format("%-35s", spousesToString());
        str += parentsToString();

        return str;
    }

    @SuppressWarnings("unused")
    public String toFormattedString2() {
        return String.format("%-30s #%-2d %-7s",
                getName().toFullName(),
                getSourceRow().getNumber(),
                getGenCode()

        );
    }


}

