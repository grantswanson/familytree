package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.List;
import java.util.Map;

public class ChildBuilder {
    public static void buildChildren(List<Marriage> marriages, Map<String, Person> individualMap) {
        // for each marriage
        for (Marriage marriage : marriages) {
            Row row = marriage.getSourceRow();
            if (marriage.isSpousesParents()) {
                // don't add children (they are already there) just one person (the spouse)
                continue;
            }
            List<String> chidrensNames = row.getChildren();
            for (int i = 0; i < chidrensNames.size(); i++) {
                Name name = Name.extractChildrensName(chidrensNames.get(i));
                String expectedCode = GenCode.buildChildsCode(row.getGenCode(), i + 1);
                Person expectedPerson = PersonMap.getPersonByGenCode(expectedCode);

                if (name == null && expectedPerson == null) {
                    continue;
                } else if (name == null) {
                    throw new RuntimeException("ln#" + row.getNumber() + " Child #" + i + " " + chidrensNames.get(i) + " is null. genCode:" +
                            expectedCode + " not sure why. person" + expectedPerson);
                } else if (expectedPerson == null) {
                    String altMarriageExpectedCode = GenCode.buildUnRelatedChildsCode(row.getGenCode(), i + 1);
                    expectedPerson = PersonMap.getPersonByGenCode(altMarriageExpectedCode);

                    if (expectedPerson != null) {
                        if (expectedPerson.hasMiscNotes()) {
                            System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.toFullName() +
                                    " FOUND under a different marriage." +
                                    " origGenCode:" + expectedCode +
                                    " altGenCode:" + altMarriageExpectedCode +
                                    " Found miscNotes:" + expectedPerson.getMiscNotes());
                        } else {
                            //System.out.println
                            throw new RuntimeException
                                    ("ln#" + row.getNumber() + " Child #" + i + " " + name.toFullName() + " SUCCESS! FOUND under a different marriage. genCode:" +
                                            altMarriageExpectedCode + " so we should Create that marriage and merge that person.");
                        }
                    }
                }

                if (expectedPerson == null) { // find by name
                    expectedPerson = PersonMap.getPersonByNameKey(name.toNameKey());
                    if (expectedPerson != null) {
                        System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.toFullName() +
                                " found BY NAME:" + expectedPerson);
                    } else {
                        System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.toNameKey() +
                                " NOT found BY NAME.");
                    }
                }

                if (expectedPerson != null) {
                    boolean success = PersonMerger.merge(expectedPerson, name, row.getNumber(), " Child #" + i, true);
                    if (success) {
                        marriage.addChild(expectedPerson, i + 1);
                    } else {
                        System.out.println(" Merge failed!!! Fix data.");
                    }
                } else {
                    if (name.isHasSpecialNote()) {
                        System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.toFullName() + " NOT found under genCode:" +
                                expectedCode + ". But note found. childrensNotes:" + row.getChildrenNotes());
                    } else {
                        System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.toFullName() + " NOT found under genCode:" +
                                expectedCode + " so we should build that person.");
                    }
                }
            }

        }
        // get the kids names
        // for each kid
        // build the gencode of the kid and look up person
        // merge the name and person
    }


}
