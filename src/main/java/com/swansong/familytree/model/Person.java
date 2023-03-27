package com.swansong.familytree.model;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
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

    private String gender="";

    @ToString.Exclude

    private Map<String, Person> spouses = new HashMap<>();

    public void addSpouse(Person spouse) {
        spouses.put(spouse.getGenCode(), spouse);
    }
    @ToString.Include
    public String spousesToString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse= entry.getValue();
            strBuilder.append(String.format("#%d %-4s %-10s", spouse.getSourceLineNumber(), spouse.getGenCode(),
                    spouse.getName().getFirstNames() + ", "));
        }
        String str = strBuilder.toString();
        if(!str.isEmpty()) {
            str =" spouses:" + str;
        }
        return str;
    }
    public void setSpousesGender(boolean isMale) {
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse= entry.getValue();
            spouse.setGenderToMale(isMale);
            spouse.setSpousesGenderNonRecursive(!isMale);
        }
    }
    private void setSpousesGenderNonRecursive(boolean isMale) {
        for (Map.Entry<String, Person> entry : spouses.entrySet()) {
            Person spouse= entry.getValue();
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
    private Person[] children = new Person[12];
    public void addChild(Person child, int i) {
        children[i] = child;
    }

    @ToString.Include
    public String childrenToString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Person child : children) {
            if(child==null) { continue;}
            strBuilder.append(String.format("#%d %s %s", child.getSourceLineNumber(), child.getGenCode(),
                    child.getName().getFirstNames() + ", "));

        }
        if (strBuilder.length() != 0) {
            strBuilder.insert(0, "  Kids:");
        }
        return strBuilder.toString();
    }


    //private String source;
//    private String birthdate;
//    private String birthplace;
//    private String birthsource = defaultSource;
//    private String baptismdate;
//    private String baptismplace;
//    private String baptismsource = defaultSource;
//    private String deathdate;
//    private String deathplace;
//    private String deathsource = defaultSource;
//    private String burialdate;
//    private String burialplace;
//    private String burialsource = defaultSource;


//    private String occupationdate;
//    private String occupationplace;
//    private String occupationplace_id;
//    private String occupationsource;
//    private String occupationdescr;
//    private String residencedate;
//    private String residenceplace;
//    private String residenceplace_id;
//    private String residencesource;
//    private String attributetype;
//    private String attributevalue;
//    private String attributesource;

}

