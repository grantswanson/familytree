package com.swansong.familytree.biz;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PersonMerger {

    public static List<String> failedMerges = new ArrayList<>();
    public static List<String> similarPersonMerges = new ArrayList<>();


    public static boolean merge(Person person, Name altName, int rowNum, String altNameSource) {
        if (person == null) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null person!!!. Check null before merging. Source:" + altNameSource);
        } else if (altName == null || altName.isBlank()) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null or blank name!!!. Check before merging. Source:" + altNameSource);
        }

        if (Name.isMergeAllowed(altName, person.getName())) {
            //System.out.println("ln#:" + rowNum + " Merged person: " + person.getName().getLastCommaFirst() +
            //                   "\n           with name: " + altName.getLastCommaFirst());
            person.getName().mergeInName(altName);
            //System.out.println("   Name of new person: " + person.getName().getLastCommaFirst());
            return true;
        } else if (Name.areNamesPossiblyMisspelled(altName, person.getName())) {
//            String s = "Merged similar names ln#:" + rowNum + " Source:" + altNameSource +
//                    "\n name from row text: '" + altName.getLastCommaFirst() + "' is SIMILAR to" +
//                    "\n name from genCode : '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
            person.getName().mergeInMisspelledName(altName, rowNum, altNameSource);
//            s += "\n  final merged name: '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
//            System.out.println(s);
            return true;
        } else {
            String s = "Merge failed ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text:'" + altName.getLastCommaFirst() + "'" +
                    "\n name from genCode :'" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
            failedMerges.add(s);
            System.out.println(s);
            return false;
        }
    }

    private static Name findRightName(Person person, Name altName) {
        Set<Name> myselfNames = getNameOfMyselfFromChildrensParent(person);
        for (Name myselfName : myselfNames) {
            if (Name.isMergeAllowed(myselfName, person.getName())) {
                System.out.println("Found right name:" + myselfName);
                //return myselfName;
            } else {
                System.out.println("Did NOT find right name:" + myselfName);
            }
            if (Name.isMergeAllowed(myselfName, altName)) {
                System.out.println("Found right name:" + myselfName);
                //return myselfName;

            } else {
                System.out.println("Did NOT find right name:" + myselfName);
            }
        }
        return null;
    }

    private static Set<Name> getNameOfMyselfFromChildrensParent(Person person) {
        Set<Name> names = new LinkedHashSet<>();
        List<Marriage> marriages = person.getMarriages();
        for (Marriage marriage : marriages) {
            Person[] children = marriage.getChildren();
            for (Person child : children) {
                if (child != null) {
                    Person father = child.getFather();
                    if (father != null) {
                        names.add(father.getName());
                    }
                    Person mother = child.getMother();
                    if (mother != null) {
                        names.add(mother.getName());
                    }
                }
            }
        }
        return names;
    }
}
