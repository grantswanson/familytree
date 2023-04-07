package com.swansong.familytree.model;

public class Id {

    private final String code;
    private int currentNum = 0;

    private Id(String code) {
        this.code = code;
    }

    public static final Id PERSON = new Id("P");
    public static final Id MARRIAGE = new Id("M");
    public static final Id INDIVIDUAL = new Id("I");
    public static final Id FAMILY = new Id("F");
    public String nextId() {
        return code + currentNum++;
    }
}
