package com.swansong.familytree.data;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
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
            String str = String.format("#%-2d %-5s ",
                    marriage.getSourceRow().getNumber(), marriage.getId());
            Person person = marriage.getHusband();
            if (person != null) {
                str += " " + person.toShortString();
            }

            person = marriage.getWife();
            if (person != null) {
                str += " " + person.toShortString();
            }

            str += marriage.childrenToString();
            System.out.println(str);
        }
    }
}
