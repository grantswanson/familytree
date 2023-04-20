package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.*;
import com.swansong.familytree.utils.StringUtils;

import java.util.ArrayList;

public class ParentBuilder {

    public static void buildParents(ArrayList<Row> csvData) {
        for (Row row : csvData) {
            boolean foundFather = false;
            boolean foundMother = false;

            Person father = null, mother = null;
            if (row.getFather() != null && !row.getFather().isBlank()) {
                Name fathersName = Name.parseFullName(row.getFather());
                father = findParent(fathersName, row, true);
                if (father == null) {
                    //System.out.println("ln#:" + row.getNumber() +" father not found! name:"+row.getFather());
                    father = PersonBuilder.buildBasicPerson(row.getFather());
                    father.setGenderToMale(true);
                    father.setGenCode(GenCode.buildUnrelatedFathersCode(row.getGenCode()));
                    father.setSourceRow(row);
                    father.addSource(Source.Parents);
//                    System.out.println("ln#:" + row.getNumber() + " created father:" + father.toShortString());

                    PersonMap.savePerson(father);
                } else {
                    foundFather = true;
                }
            }

            if (row.getMother() != null && !row.getMother().isBlank()) {
                Name mothersName = Name.parseFullName(row.getMother());
                mother = findParent(mothersName, row, false);
                if (mother == null) {
                    //System.out.println("ln#:" + row.getNumber() +" mother not found! name:"+row.getMother());
                    mother = PersonBuilder.buildBasicPerson(row.getMother());
                    mother.setGenderToMale(false);
                    mother.setGenCode(GenCode.buildUnrelatedMothersCode(row.getGenCode()));
                    mother.setSourceRow(row);
                    mother.addSource(Source.Parents);

                    //System.out.println("ln#:" + row.getNumber() + " created mother:" + mother.toShortString());

                    PersonMap.savePerson(mother);
                } else {
                    foundMother = true;
                }
            }
            if (father != null || mother != null) {
                if ((foundFather || father == null) && (foundMother || mother == null)) { // existing people
                    MarriageMerger.verifyExistingMarriage(father, mother,
                            Source.Parents, null, row);

                } else { // new people, so must be new marriage
                    Marriage marriage = MarriageBuilder.buildMarriage(father, mother, row, Source.Parents);
                    MarriageMap.addMarriage(marriage);

                    // if unrelated child, then add child to marriage (they won't get added to the marriage later)
                    if (GenCode.isUnrelated(row.getGenCode())) {
                        Person mainPerson = PersonMap.getPersonByGenCode(row.getGenCode());
                        marriage.addChild(mainPerson);
                        mainPerson.setFather(father);
                        mainPerson.setMother(mother);
                    }

                }
            }
        }
    }


    private static Person findParent(Name parentsName, Row row, boolean isFather) {
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
        if (!success) {// && parentsName.hasSurnameAndOtherNames()) {
            expectedParent = PersonMap.getPersonByNameKey(parentsName.toNameKey());
            if (expectedParent != null) {
                success = PersonMerger.merge(expectedParent, parentsName, row.getNumber(), fatherMotherStr + "  merge parent2", false);
            }
        }

        if (success) {
            expectedParent.setGenderToMale(isFather);
            expectedParent.setSpousesGender(!isFather);
            if ((StringUtils.isEndingWithNumber(row.getGenCode()) && !row.getGenCode().endsWith("1")) ||
                    GenCode.isUnrelated(row.getGenCode())) {
                expectedParent.addSource(Source.ParentsOfUnRelatedChildren);
                expectedParent.removeSource(Source.Parents);

            }
            return expectedParent;
        }
        return null;
    }


}
