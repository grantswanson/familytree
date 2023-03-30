package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParentAndChildBuilder {

    public static void mergeInParentsAndChildren(Row row, Map<String, Person> individualMap) {
        if (row.getFather() != null && !row.getFather().isBlank()) {
            Name fathersName = Name.parseLastCommaFirstName(row.getFather());
            mergeInParentsAndChildren(fathersName, row, individualMap, true);
        }

        if (row.getMother() != null && !row.getMother().isBlank()) {
            Name mothersName = Name.parseLastCommaFirstName(row.getMother());
            mergeInParentsAndChildren(mothersName, row, individualMap, false);
        }
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
            String fatherMotherStr = (isFather ? "Father" : "Mother");
            if (parent1 != null && Name.areNamesPossiblyMisspelled(parentsName, parent1.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :" + parentsName.getLastCommaFirst() + " is SIMILAR to" +
                        "\n parent1:" + parent1.getName().getLastCommaFirst());

            } else if (parent2 != null && Name.areNamesPossiblyMisspelled(parentsName, parent2.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:" + row.getNumber() + " is SIMILAR to" +
                        "\n " + fatherMotherStr + " :" + parentsName.getLastCommaFirst() +
                        "\n parent2:" + parent2.getName().getLastCommaFirst());
            } else {
                System.out.println(fatherMotherStr + " not found in main list. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :" + parentsName.getLastCommaFirst() +
                        "\n Parent1:" + (parent1 == null ? "null" : parent1.getName().getLastCommaFirst()) +
                        "\n Parent2:" + (parent2 == null ? "null" : parent2.getName().getLastCommaFirst()));
            }
        }
    }

    private static void addChild(Person parent, Row row, Map<String, Person> individualMap) {
        int num = GenCode.getChildNumber(row.getGenCode()); //num is base 1, not base 0;
        Person child = individualMap.get(GenCode.buildSelfCode(row.getGenCode()));
        parent.addChild(child, num);
        if (parent.isMale()) {
            child.setFather(parent);
        } else {
            child.setMother(parent);
        }
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
            Name name = Name.extractChildrensName(row.getChild(i));
            if (name != null) {
                names.add(name);
            }
        }
        return names;
    }


    public static Marriage addSpousesParents(Map<String, Person> individualMap, Row row, Person spouse) {
        Person spousesFather = buildSpousesFather(individualMap, row);
        if (spousesFather != null) {
            spouse.setFather(spousesFather);
            spousesFather.setChildren(new Person[]{spouse});
        }
        Person spousesMother = buildSpousesMother(individualMap, row);
        if (spousesMother != null) {
            spouse.setMother(spousesMother);
            spousesMother.setChildren(new Person[]{spouse});
        }
        if (spousesMother != null && spousesFather != null) {
            spousesMother.addSpouse(spousesFather);
            spousesFather.addSpouse(spousesMother);
            return MarriageBuilder.buildMarriage(spousesFather, spousesMother, row);
        }
        return null;
    }

    public static Person buildSpousesFather(Map<String, Person> individualMap, Row row) {
        Person existingSpousesFather = individualMap.get(GenCode.buildSpousesFatherCode(row.getGenCode()));

        if (existingSpousesFather == null) {
            // make new person
            existingSpousesFather = buildSpousesFather(row);
            if (existingSpousesFather != null) {
                individualMap.put(existingSpousesFather.getGenCode(), existingSpousesFather);
            }
        } else {
            existingSpousesFather.appendDebug(" Also SpousesFather ln#:" + row.getNumber());
        }
        return existingSpousesFather;
    }

    public static Person buildSpousesMother(Map<String, Person> individualMap, Row row) {
        Person existingSpousesMother = individualMap.get(GenCode.buildSpousesMotherCode(row.getGenCode()));
        if (existingSpousesMother == null) {
            // make new person
            existingSpousesMother = buildSpousesMother(row);
            if (existingSpousesMother != null) {
                individualMap.put(existingSpousesMother.getGenCode(), existingSpousesMother);
            }
        } else {
            existingSpousesMother.appendDebug(" Also SpousesMother ln#:" + row.getNumber());
        }
        return existingSpousesMother;
    }

    public static Person buildSpousesFather(Row row) {
        String name = row.getSpouseFather();
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonAndSpouseBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenderToMale(true);
        person.setGenCode(GenCode.buildSpousesFatherCode(row.getGenCode()));

        return person;
    }

    public static Person buildSpousesMother(Row row) {
        String name = row.getSpouseMother();
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonAndSpouseBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenderToMale(false);
        person.setGenCode(GenCode.buildSpousesMotherCode(row.getGenCode()));

        return person;
    }


}
