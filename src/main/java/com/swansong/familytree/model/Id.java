package com.swansong.familytree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;


public class Id {

    private String code;
    private int currentNum = 0;
    private Id (String code) {
        this.code = code;
    }
    public static Id INDIVIDUAL = new Id("I");
    public static Id MARRIAGE = new Id("M");
    public static Id FAMILY = new Id("F");

    public String nextId() {
        return "["+code+currentNum++ +"]";
    }
}
