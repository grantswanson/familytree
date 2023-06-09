package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.swansong.familytree.utils.StringUtils.addCommaIfMissing;

public class ChildBuilder {
    public static final int MAX_CHILDREN = 12;

    public static void buildChildren() {
        // for each marriage
        for (Marriage marriage : MarriageMap.getMarriagesCollection()) {
            Row row = marriage.getSourceRow();
            if (marriage.getSources().contains(Source.SpousesParents) ||
                    marriage.getSources().contains(Source.Parents)) {
                // don't add children they will be added earlier
                // for Parents, it will be when processing parents row as the main person
                // for Spouse's parent, the spouse will be added as a child when creating the spouse's parents
                continue;
            }
            List<Name> chidrensNames = extractChildrensNames(row);
            for (int i = 0; i < chidrensNames.size(); i++) {
                Name childsName = chidrensNames.get(i);
                if (childsName != null) {
                    buildChild(marriage, row, childsName, i);
                }
            }
        }
    }


    private static void buildChild(Marriage marriage, Row row, Name childsName, int i) {
        boolean foundUnrelatedChild = false;

        // build the genCode of the kid and look up person
        // merge the name and person
        String expectedCode = GenCode.buildChildsCode(row.getGenCode(), i + 1);
        Person expectedPerson = PersonMap.getPersonByGenCode(expectedCode);
        if (!parentsMatch(row, expectedPerson)) {
            expectedPerson = null;
        }

        if (expectedPerson == null) {
            // possibly add or removed 1 at the end of the code
            expectedCode = GenCode.buildAltCode(expectedCode);
            expectedPerson = PersonMap.getPersonByGenCode(expectedCode);
            if (!parentsMatch(row, expectedPerson)) {
                expectedPerson = null;
            }
        }
        if (expectedPerson == null) {
            String altMarriageExpectedCode = GenCode.buildUnRelatedChildsCode(row.getGenCode(), i + 1);
            expectedPerson = PersonMap.getPersonByGenCode(altMarriageExpectedCode);
            if (!parentsMatch(row, expectedPerson)) {
                expectedPerson = null;
            }

            if (expectedPerson != null) {
                foundUnrelatedChild = true;

//                System.out.println("Warn: ln#" + row.getNumber() + " Child #" + i + " " + childsName.toFullName() +
//                        " FOUND under a different marriage. Storing child as unrelated." +
//                        "\n  origGenCode:" + expectedCode +
//                        "\n  altGenCode :" + altMarriageExpectedCode +
//                        (expectedPerson.getChildrenNotes() != null ? "\n  miscNotes:" + expectedPerson.getChildrenNotes() : ""));
                if (!expectedPerson.hasChildRelatedNotes()) {
                    throw new RuntimeException
                            ("expected miscNotes!");
                }
            }
        }

        if (expectedPerson == null) { // find by name
            setChildsSurName(marriage, childsName);
            expectedPerson = PersonMap.getPersonByNameKey(childsName.toNameKey());
            if (!parentsMatch(row, expectedPerson)) {
                expectedPerson = null;
            }
//            if (expectedPerson != null) {
//                // comment out because parent match
//                System.out.println("Warn: ln#" + row.getNumber() + " Child #" + i + " " + childsName.toFullName() +
//                        "\n       found BY NAME:" + expectedPerson.getName().toFullName() +
//                        "\n  expectedGenCode:" + expectedCode +
//                        "\n    foundGenCode :" + expectedPerson.getGenCode()
//                );
//            }
        }

        if (expectedPerson != null) {
            boolean success = PersonMerger.merge(expectedPerson, childsName, row.getNumber(), " Child #" + i, true);
            if (success) {
                if (foundUnrelatedChild) {
                    marriage.addChildFromUnRelatedMarriage(expectedPerson);
                } else {
                    expectedPerson.setFather(marriage.getHusband());
                    expectedPerson.setMother(marriage.getWife());
                    marriage.addChild(expectedPerson, i + 1);
                    marriage.addSource(Source.Parents);
                }

            } else {
                throw new RuntimeException(" Merge failed!!! Fix data.");
            }
        } else {
            Person parent = marriage.getSpouse2();
            if (parent == null) {
                parent = marriage.getSpouse1();
            }

            parent.addProcessingNote(
                    childsName.toFullName() + " is possibly child #" + (i + 1) + " and was accidentally not added or is from a different marriage.");
//            System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + childsName.toNameKey() +
//                    " NOT found by GenCode, AltGenCode, or Name. " +
//                    (childsName.isAsteriskPresent() ? "\n childsName has asterisk." : "") +
//                    (row.hasChildrenNotes() ? "\n " + row.getChildrenNotes() : "")
//            );
            if (!childsName.isAsteriskPresent() && !row.hasChildrenNotes()) {
                System.out.println
                        //throw new RuntimeException
                                ("expected notes or asterisk!!!!!\n\n");
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean parentsMatch(Row row, Person expectedPerson) {
        if (expectedPerson == null) {
            return false;
        }
        String fathersGenCode = expectedPerson.getFather() != null ? expectedPerson.getFather().getGenCode() : "";
        String mothersGenCode = expectedPerson.getMother() != null ? expectedPerson.getMother().getGenCode() : "";
        if (fathersGenCode.isEmpty() && mothersGenCode.isEmpty()) {
            return true;
        } else if (!GenCode.isEqual(row.getGenCode(), fathersGenCode) &&
                !GenCode.isEqual(row.getGenCode(), mothersGenCode)) {
            System.out.println("Warn: ln#" + row.getNumber() + " Parents do NOT match! " +
                    "\n fathersGenCode:" + fathersGenCode +
                    "\n mothersGenCode:" + mothersGenCode +
                    "\n    row GenCode:" + row.getGenCode());
            return false;
        } else {
            return true;
        }
    }

    private static void setChildsSurName(Marriage marriage, Name childsName) {
        String surName = marriage.getChildrensSurName();
        //System.out.println("surName:"+surName);
        if (surName != null && childsName.getSurName().isBlank()) {
            childsName.setSurName(surName);
        }
    }

    /**
     * @param i Base 1. Not base 0
     */
    public static void verifyChildNumber(int i) {
        if (i <= 0 || i > MAX_CHILDREN) {
            throw new IllegalArgumentException("Invalid child#:" + i + " It must be >0 and <=" + MAX_CHILDREN);
        }
    }

    public static List<Name> extractChildrensNames(Row row) {
        List<Name> childrensNames = new ArrayList<>();
        List<String> names = row.getChildren();

        for (String nameStr : names) {
            Name name = extractChildrensName(nameStr);
            if (name != null) {
                childrensNames.add(name);
            }
        }

        return childrensNames;
    }

    public static Name extractChildrensName(String name) {
        if (name != null && !name.replace("*", "").isBlank()) {
            return Name.parseFullName(
                    addCommaIfMissing(name.trim()));

        }
        return null;
    }
}
