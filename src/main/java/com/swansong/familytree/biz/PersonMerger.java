package com.swansong.familytree.biz;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

public class PersonMerger {


    @SuppressWarnings({"unused", "UnusedAssignment"})
    public static boolean merge(Person person, Name altName, int rowNum, String altNameSource) {
        if (person == null) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null person!!!. Check null before merging. Source:" + altNameSource);
        } else if (altName == null || altName.isBlank()) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null or blank name!!!. Check before merging. Source:" + altNameSource);
        }

        if (Name.isMergeAllowed(altName, person.getName())) {
            String s = "ln#:" + rowNum + " Merged person: " + person.getName().getLastCommaFirst() +
                    "\n           with name: " + altName.getLastCommaFirst();
            person.getName().mergeInName(altName);
            s += "\n   Name of new person: " + person.getName().getLastCommaFirst();
//            System.out.println(s);
            return true;
        } else if (person.getName().startsWith(altName)) {
            String s = "Merged names that start with the other. (also handles initials)  ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text: '" + altName.getLastCommaFirst() + "' is SIMILAR to" +
                    "\n name from genCode : '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
            person.getName().mergeStartsWith(altName, rowNum, altNameSource);
            s += "\n  final merged name: '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
//            System.out.println(s);
            return true;
        } else if (Name.areNamesPossiblyMisspelled(altName, person.getName())) {
            String s = "Merged similar names ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text: '" + altName.getLastCommaFirst() + "' is SIMILAR to" +
                    "\n name from genCode : '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
            person.getName().mergeInMisspelledName(altName, rowNum, altNameSource);
            s += "\n  final merged name: '" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
//            System.out.println(s);
            return true;

        } else {
            String s = "Merge failed ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text:'" + altName.getLastCommaFirst() + "'" +
                    "\n name from genCode :'" + person.getName().getLastCommaFirst() + "' " + person.getGenCode();
            System.out.println(s);
            return false;
        }
    }

}
