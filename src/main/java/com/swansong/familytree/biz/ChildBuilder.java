package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.List;

import static com.swansong.familytree.StringUtilities.addCommaIfMissing;

public class ChildBuilder {
    public static final int MAX_CHILDREN = 12;

    public static void buildChildren() {
        // for each marriage
        for (Marriage marriage : MarriageMap.getMarriagesCollection()) {
            Row row = marriage.getSourceRow();
            if (marriage.isSpousesParents()) {
                // don't add children (they are already there) just one person (the spouse)
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
        // build the genCode of the kid and look up person
        // merge the name and person
        String expectedCode = GenCode.buildChildsCode(row.getGenCode(), i + 1);
        Person expectedPerson = PersonMap.getPersonByGenCode(expectedCode);

        if (expectedPerson == null) {
            String altMarriageExpectedCode = GenCode.buildUnRelatedChildsCode(row.getGenCode(), i + 1);
            expectedPerson = PersonMap.getPersonByGenCode(altMarriageExpectedCode);

            if (expectedPerson != null) {
                marriage.addChildFromUnRelatedMarriage(expectedPerson);

                System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + childsName.toFullName() +
                        " FOUND under a different marriage." +
                        "\n origGenCode:" + expectedCode +
                        "\n altGenCode :" + altMarriageExpectedCode +
                        (expectedPerson.hasMiscNotes() ? " miscNotes:" + expectedPerson.getMiscNotes() : ""));
                if (!expectedPerson.hasMiscNotes()) {
                    //System.out.println
                    throw new RuntimeException
                            ("expected miscNotes!");
                }
            }
        }

        if (expectedPerson == null) { // find by name
            setChildsSurName(marriage, childsName);
            expectedPerson = PersonMap.getPersonByNameKey(childsName.toNameKey());
            if (expectedPerson != null) {
                System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + childsName.toFullName() +
                        " found BY NAME:" + expectedPerson);
            }
        }

        if (expectedPerson != null) {
            boolean success = PersonMerger.merge(expectedPerson, childsName, row.getNumber(), " Child #" + i, true);
            if (success) {
                expectedPerson.setFather(marriage.getHusband());
                expectedPerson.setMother(marriage.getWife());

                marriage.addChild(expectedPerson, i + 1);

            } else {
                //System.out.println
                throw new RuntimeException(" Merge failed!!! Fix data.");
            }
        } else {
            System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + childsName.toNameKey() +
                    " NOT found by GenCode, AltGenCode, or Name. " +
                    (childsName.isAsteriskPresent() ? "\n childsName has asterisk." : "") +
                    (row.hasChildrenNotes() ? "\n " + row.getChildrenNotes() : "")
            );
            if (!childsName.isAsteriskPresent() && !row.hasChildrenNotes()) {
                System.out.println
                        //throw new RuntimeException
                                ("expected notes or asterisk!!!!!\n\n");
            }
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
