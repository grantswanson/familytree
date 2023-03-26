package com.swansong.familytree.model;

import lombok.Data;

@Data
public class Marriage {

    private final static String defaultSource = "Donovan Burdette Meyer";

    public Marriage() {
        id = Id.MARRIAGE.nextId();
    }
    private String id;
    private Integer sourceLineNumber;
    private Person spouse1;
    private Person spouse2;

    public Person getHusband() {
        if(spouse2.isMale()) {
            return spouse2;
        } else {
            return spouse1;
        }
    }
    public Person getWife() {
        if(!spouse2.isMale()) {
            return spouse2;
        } else {
            return spouse1;
        }
    }
}
