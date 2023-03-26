package com.swansong.familytree.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Person {
    private final static String defaultSource = "Donovan Burdette Meyer";

    public Person() {
        id = Id.INDIVIDUAL.nextId();
    }


    private String id;
    private Integer sourceLineNumber;
    private String genCode;

    private Name name;

    private String gender="";

    private Map<String, Person> spouses = new HashMap<>();

    public void addSpouse(Person spouse) {
        spouses.put(spouse.getGenCode(), spouse);
    }

    public void setGenderToMale(boolean isMale) {
        if (isMale) {
            gender = "M";
        } else {
            gender = "F";
        }
    }

    public void appendDebug(String s) {
        debug += s;
    }

    private String debug = "";


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

