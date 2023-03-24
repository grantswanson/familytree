package com.swansong.familytree.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private final String defaultSource = "Donovan Burdette Meyer";
    private GrampaMeyerGenCode grandpaMeyerCode;

    private Name name;

    private String gender;
    //private String source;
    private String birthdate;
    private String birthplace;
    private String birthsource=defaultSource;
    private String baptismdate;
    private String baptismplace;
    private String baptismsource=defaultSource;
    private String deathdate;
    private String deathplace;
    private String deathsource=defaultSource;
    private String burialdate;
    private String burialplace;
    private String burialsource=defaultSource;
    private String note;

    public String  getKey() {
        return name.getLastCommaFirst();
    }
    public String getKeyAndGenCode() {
        return getKey()+" "+grandpaMeyerCode;
    }
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

