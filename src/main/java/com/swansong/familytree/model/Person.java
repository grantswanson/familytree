package com.swansong.familytree.model;

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
        id = Id.INDIVIDUAL.nextId();
    }

    private String id;
    private Integer sourceLineNumber;
    private String genCode;
    private Name name;
    private String gender = "";

    private Name fathersName;
    private Person father;
    private Name mothersName;
    private Person mother;
    private String dob = ""; //format is yyyy MMM d(for MOST dates, some are just yyyy)
    private String pob = "";
    private String baptismDate = ""; //format yyyy or yyyy MMM d
    private String baptismPlace = "";
    private String confirmationDate = "";
    private String confirmationPlace = "";
    private String deathDate = "";
    private String deathPlace = "";
    private String burialDate = "";
    private String burialPlace = "";

    @ToString.Exclude

    private Map<String, Person> spouses = new HashMap<>();


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
            strBuilder.append(String.format("#%d %-4s %-14.14s", spouse.getSourceLineNumber(), spouse.getGenCode(),
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

    private String debug = "";


    @ToString.Exclude
    List<Marriage> marriages = new ArrayList<>();

    public Marriage getMarriage(int i) {
        return marriages.get(i);
    }

    public void addMarriage(Marriage marriage) {
        marriages.add(marriage);
    }


    //private String source;
    private String birthSource = defaultSource;
    private String baptismSource = defaultSource;
    private String deathSource = defaultSource;
    private String burialSource = defaultSource;
    private String occupationSource = defaultSource;

    public String parentsToString() {
        String str = "";
        if (father != null) {
            str = " Dad:" + father.getGenCode() + " " + father.getName().toFullName();
        }
        if (mother != null) {
            str += " Mom:" + mother.getGenCode() + " " + mother.getName().toFullName();
        }
        if (fathersName != null) {
            str = " Dad:" + fathersName.toFullName();
        }
        if (mothersName != null) {
            str += " Mom:" + mothersName.toFullName();
        }
        return str;
    }


    public String toShortString() {
        return String.format("#%d %-5s %-1s %-30.30s",
                sourceLineNumber, genCode, gender,
                name.toFullName());
    }

//    private String occupationdate;
//    private String occupationplace;
//    private String occupationplace_id;
//
//    private String occupationdescr;
//    private String residencedate;
//    private String residenceplace;
//    private String residenceplace_id;
//    private String residencesource;
//    private String attributetype;
//    private String attributevalue;
//    private String attributesource;

}

