package com.swansong.familytree.data;

import com.swansong.familytree.model.Marriage;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MarriageMap {
    public static List<Marriage> marriages = new LinkedList<>();

    public static void addMarriage(Marriage marriage) {
        marriages.add(marriage);
    }

    public static List<Marriage> getMarriagesList() {
        return marriages;
    }

    public static void printMarriages() {
        System.out.println("\nMarriages...");
        // build the marriages
        for (Marriage marriage : marriages) {

            String str = marriage.toFormattedString();

            System.out.println(str);
        }
    }

}
