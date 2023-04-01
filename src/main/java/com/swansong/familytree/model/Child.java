package com.swansong.familytree.model;

import com.swansong.familytree.csv.Row;

import java.util.ArrayList;
import java.util.List;

public class Child {
    public static final int MAX_CHILDREN = 12;

    /**
     * @param i Base 1. Not base 0
     */
    public static void verifyChildNumber(int i) {
        if (i <= 0 || i > MAX_CHILDREN) {
            throw new IllegalArgumentException("Invalid child#:" + i + " It must be >0 and <=" + MAX_CHILDREN);
        }
    }

    public static List<Name> buildChildrensNames(Row row) {
        List<Name> childrensNames = new ArrayList<>();
        List<String> names = row.getChildren();

        for (String nameStr : names) {
            Name name = Name.extractChildrensName(nameStr);
            childrensNames.add(name);
        }

        return childrensNames;
    }
}
