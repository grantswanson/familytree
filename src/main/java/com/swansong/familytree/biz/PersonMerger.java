package com.swansong.familytree.biz;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.model.Source;

public class PersonMerger {


    @SuppressWarnings({"unused", "UnusedAssignment"})
    public static boolean merge(Person person, Name altName, int rowNum, String altNameSource, boolean expectMerge) {
        if (person == null) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null person!!!. Check null before merging. Source:" + altNameSource);
        } else if (altName == null || altName.isBlank()) {
            throw new RuntimeException("ln#:" + rowNum +
                    " Trying to merge a null or blank name!!!. Check before merging. Source:" + altNameSource);
        }

        if (Name.isMergeAllowed(altName, person.getName())) {
            String s = "ln#:" + rowNum + " Merged person: " + person.getName().toFullName() +
                    "\n           with name: " + altName.toFullName();
            person.getName().mergeInName(altName);
            s += "\n   Name of new person: " + person.getName().toFullName();
//            System.out.println(s);
            person.addSource(Source.Children);
            return true;
        } else if (person.getName().startsWith(altName)) {
            String s = "Merged names that start with the other. (also handles initials)  ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text: '" + altName.toFullName() + "' is SIMILAR to" +
                    "\n name from genCode : '" + person.getName().toFullName() + "' " + person.getGenCode();
            person.getName().mergeStartsWith(altName);
            s += "\n  final merged name: '" + person.getName().toFullName() + "' " + person.getGenCode();
//            System.out.println(s);
            person.addSource(Source.Children);
            return true;
        } else if (Name.areNamesPossiblyMisspelled(altName, person.getName())) {
            String s = "Merged similar names ln#:" + rowNum + " Source:" + altNameSource +
                    "\n name from row text: '" + altName.toFullName() + "' is SIMILAR to" +
                    "\n name from genCode : '" + person.getName().toFullName() + "' " + person.getGenCode();
            person.getName().mergeInMisspelledName(altName);
            s += "\n  final merged name: '" + person.getName().toFullName() + "' " + person.getGenCode();
//            System.out.println(s);
            person.addSource(Source.Children);
            return true;

        } else {
            String s = "ln#:" + rowNum + " Source:" + altNameSource + " Merge failed. ExpectedSuccess:" + expectMerge +
                    "\n name from row text:'" + altName.toFullName() + "'" +
                    "\n name from genCode :'" + person.getName().toFullName() + "' " + person.getGenCode();
            //System.out.println(s);

            if (expectMerge) {
                //System.out.println(s);
                throw new RuntimeException(s);
            }
            return false;

        }
    }

}
