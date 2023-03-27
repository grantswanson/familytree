package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParentAndChildBuilder {

    public static void mergeInParentsAndChildren(Row row, Map<String, Person> individualMap) {
        Name fathersName = Name.parseLastCommaFirstName(row.getFather());
        mergeInParentsAndChildren(fathersName, row, individualMap, true);

        Name mothersName = Name.parseLastCommaFirstName(row.getMother());
        mergeInParentsAndChildren(mothersName, row, individualMap, false);
    }

    private static void mergeInParentsAndChildren(Name parentsName, Row row, Map<String, Person> individualMap, boolean isFather) {
        if (parentsName.isBlank()) {
            return;
        }
        Person parent1 = individualMap.get(GenCode.buildParent1Code(row.getGenCode()));
        Person parent2 = individualMap.get(GenCode.buildParent2Code(row.getGenCode()));

        if (parent1 != null && Name.isMergeAllowed(parentsName, parent1.getName())) {
            parent1.getName().mergeInName(parentsName);
            parent1.setGenderToMale(isFather);
            parent1.setSpousesGender(!isFather);
            addChild(parent1, row, individualMap);
        } else if (parent2 != null && Name.isMergeAllowed(parentsName, parent2.getName())) {
            parent2.getName().mergeInName(parentsName);
            parent2.setGenderToMale(isFather);
            parent2.setSpousesGender(!isFather);
            addChild(parent2, row, individualMap);
        } else {
            System.out.println((isFather ? "Father" : "Mother") + " not found in main list. ln#:" + row.getNumber() +
                    "\n " + (isFather ? "Father" : "Mother") + ":" + parentsName.getLastCommaFirst() +
                    "\n Found Parent1:" + (parent1 == null ? "null" : parent1.getName().getLastCommaFirst()) +
                    "\n Found Parent2:" + (parent2 == null ? "null" : parent2.getName().getLastCommaFirst()));
        }
    }

    private static void addChild(Person parent, Row row, Map<String, Person> individualMap) {
        int num = GenCode.getChildNumber(row.getGenCode()); //num is base 1, not base 0;
        Person child = individualMap.get(GenCode.buildSelfCode(row.getGenCode()));
        parent.addChild(child, num);
    }


    public static void mergeInChildren(Row row, Map<String, Person> individualMap) {

        List<Name> names = extractChildrensNames(row);
        Person self = individualMap.get(row.getGenCode());
        int i = 1;    // "i" is Base 1. Not base 0
        for (Name name : names) {
            mergeInChildren(i, name, self.getChild(i++), row);
        }

    }

    /**
     * @param i Base 1. Not base 0
     */
    private static void mergeInChildren(int i, Name childsName, Person child, Row row) {
        if (childsName.isBlank()) {
            return;
        }
        if (child != null && Name.isMergeAllowed(childsName, child.getName())) {
            child.getName().mergeInName(childsName);
        } else {
            System.out.println("Child list not match. ln#:" + row.getNumber() + " child#:" + i +
                    "\n childsname (from row of self):'" + childsName.getLastCommaFirst() + "'" +
                    "\n Child:" + (child == null ? "null" : "'" + child.getName().getLastCommaFirst() + "'"));
        }
    }

    public static List<Name> extractChildrensNames(Row row) {
        List<Name> names = new ArrayList<>();

        for (int i = 1; i <= Person.MAX_CHILDREN; i++) {
            String name = row.getChild(i);
            if (name != null && !name.isBlank()) {
                names.add(Name.parseLastCommaFirstName(
                        Name.addCommaIfMissing(
                                Name.removeAsterisk(name))));
            }
        }
        return names;
    }

    public Person buildSpouseFather(Row row) {
        String name = row.getSpouseFather();
        if (name == null || name.isBlank())
            return null;

        Person person = new Person();// buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGenCode("FatherInLawOf:" + row.getGenCode());

        return person;
    }

    public Person buildSpouseMother(Row row) {
        String name = row.getSpouseMother();
        if (name == null || name.isBlank())
            return null;

        Person person = new Person();// buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGenCode("MotherInLawOf:" + row.getGenCode());

        return person;
    }

}
