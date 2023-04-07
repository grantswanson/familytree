package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;

public class ParentBuilder {

    public static void buildParents(ArrayList<Row> csvData) {
        for (Row row : csvData) {
            boolean createMarriage = false;

            Person father = null, mother = null;
            if (row.getFather() != null && !row.getFather().isBlank() && !Name.isOnlySurname(row.getFather())) {
                Name fathersName = Name.parseFullName(row.getFather());
                father = buildParent(fathersName, row, true);
                if (father == null) {
                    //System.out.println("ln#:" + row.getNumber() +" father not found! name:"+row.getFather());
                    father = PersonBuilder.buildBasicPerson(row.getFather());
                    father.setGenderToMale(true);
                    father.setGenCode(GenCode.buildUnrelatedFathersCode(row.getGenCode()));
                    father.setSourceRow(row);
                    createMarriage = true;
                    System.out.println("ln#:" + row.getNumber() + " created father:" + father.toShortString());

                    PersonMap.savePerson(father);

                }
            }

            if (row.getMother() != null && !row.getMother().isBlank()) { // for mother allow only surname && !Name.isOnlySurname(row.getMother())) {
                Name mothersName = Name.parseFullName(row.getMother());
                mother = buildParent(mothersName, row, false);
                if (mother == null) {
                    //System.out.println("ln#:" + row.getNumber() +" mother not found! name:"+row.getMother());
                    mother = PersonBuilder.buildBasicPerson(row.getMother());
                    mother.setGenderToMale(false);
                    mother.setGenCode(GenCode.buildUnrelatedMothersCode(row.getGenCode()));
                    mother.setSourceRow(row);
                    createMarriage = true;
                    System.out.println("ln#:" + row.getNumber() + " created mother:" + mother.toShortString());

                    PersonMap.savePerson(mother);

                }
            }

            Marriage marriage; // find marriage
            //if (marriage == null ) { // add a marriage if it does not exist already

            if (createMarriage) {
                marriage = MarriageBuilder.buildMarriage(father, mother, row);
                MarriageMap.addMarriage(marriage);
            }

        }
    }


    private static Person buildParent(Name parentsName, Row row, boolean isFather) {
        String fatherMotherStr = (isFather ? "Father" : "Mother");
        Person expectedParent = PersonMap.getPersonByGenCode(GenCode.buildParent1Code(row.getGenCode()));

        boolean success = false;
        if (expectedParent != null) {
            success = PersonMerger.merge(expectedParent, parentsName, row.getNumber(), fatherMotherStr + " merge parent1", false);
        }

        if (!success) {
            expectedParent = PersonMap.getPersonByGenCode(GenCode.buildParent2Code(row.getGenCode()));
            if (expectedParent != null) {
                success = PersonMerger.merge(expectedParent, parentsName, row.getNumber(), fatherMotherStr + "  merge parent2", false);
            }
        }
        if (!success) {
            expectedParent = PersonMap.getPersonByNameKey(parentsName.toNameKey());
            if (expectedParent != null) {
                success = PersonMerger.merge(expectedParent, parentsName, row.getNumber(), fatherMotherStr + "  merge parent2", false);
            }
        }

        if (success) {
            expectedParent.setGenderToMale(isFather);
            expectedParent.setSpousesGender(!isFather);
            return expectedParent;
        }
        return null;
    }





}
