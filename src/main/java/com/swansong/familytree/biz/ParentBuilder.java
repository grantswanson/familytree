package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.Map;

public class ParentBuilder {

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
            ChildBuilder.addChild(parent1, row, individualMap);
        } else if (parent2 != null && Name.isMergeAllowed(parentsName, parent2.getName())) {
            parent2.getName().mergeInName(parentsName);
            parent2.setGenderToMale(isFather);
            parent2.setSpousesGender(!isFather);
            ChildBuilder.addChild(parent2, row, individualMap);
        } else {
            String fatherMotherStr = (isFather ? "Father" : "Mother");
            if (parent1 != null && Name.areNamesPossiblyMisspelled(parentsName, parent1.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "' is SIMILAR to" +
                        "\n parent1:'" + parent1.getName().getLastCommaFirst() + "' " + parent1.getGenCode());

            } else if (parent2 != null && Name.areNamesPossiblyMisspelled(parentsName, parent2.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:'" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "' is SIMILAR to" +
                        "\n parent2:'" + parent2.getName().getLastCommaFirst() + "' " + parent2.getGenCode());
            } else {
                System.out.println(fatherMotherStr + " not found in main list. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "'" +
                        "\n Parent1:'" + (parent1 == null ? "null" : parent1.getName().getLastCommaFirst() + "' " + parent1.getGenCode()) +
                        "\n Parent2:'" + (parent2 == null ? "null" : parent2.getName().getLastCommaFirst() + "' " + parent2.getGenCode()));
            }
        }
    }

    private static void mergePeople(Name parentsName, Row row, Map<String, Person> individualMap, boolean isFather) {
        if (parentsName.isBlank()) {
            return;
        }
        Person parent1 = individualMap.get(GenCode.buildParent1Code(row.getGenCode()));
        Person parent2 = individualMap.get(GenCode.buildParent2Code(row.getGenCode()));

        if (parent1 != null && Name.isMergeAllowed(parentsName, parent1.getName())) {
            parent1.getName().mergeInName(parentsName);
            parent1.setGenderToMale(isFather);
            parent1.setSpousesGender(!isFather);
            ChildBuilder.addChild(parent1, row, individualMap);
        } else if (parent2 != null && Name.isMergeAllowed(parentsName, parent2.getName())) {
            parent2.getName().mergeInName(parentsName);
            parent2.setGenderToMale(isFather);
            parent2.setSpousesGender(!isFather);
            ChildBuilder.addChild(parent2, row, individualMap);
        } else {
            String fatherMotherStr = (isFather ? "Father" : "Mother");
            if (parent1 != null && Name.areNamesPossiblyMisspelled(parentsName, parent1.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "' is SIMILAR to" +
                        "\n parent1:'" + parent1.getName().getLastCommaFirst() + "' " + parent1.getGenCode());

            } else if (parent2 != null && Name.areNamesPossiblyMisspelled(parentsName, parent2.getName())) {
                System.out.println(fatherMotherStr + " POSSIBLE MISSPELLING. ln#:'" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "' is SIMILAR to" +
                        "\n parent2:'" + parent2.getName().getLastCommaFirst() + "' " + parent2.getGenCode());
            } else {
                System.out.println(fatherMotherStr + " not found in main list. ln#:" + row.getNumber() +
                        "\n " + fatherMotherStr + " :'" + parentsName.getLastCommaFirst() + "'" +
                        "\n Parent1:'" + (parent1 == null ? "null" : parent1.getName().getLastCommaFirst() + "' " + parent1.getGenCode()) +
                        "\n Parent2:'" + (parent2 == null ? "null" : parent2.getName().getLastCommaFirst() + "' " + parent2.getGenCode()));
            }
        }
    }

}
