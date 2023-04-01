package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChildBuilder {
    public static void buildChildren(List<Marriage> marriages, Map<String, Person> individualMap) {
        // for each marriage
        for (Marriage marriage : marriages) {
            Row row = marriage.getSourceRow();
            List<String> chidrensNames = row.getChildren();
            for (int i = 0; i < chidrensNames.size(); i++) {
                Name name = Name.extractChildrensName(chidrensNames.get(i));
                String expectedCode = GenCode.buildChildsCode(row.getGenCode(), i + 1);
                Person expectedPerson = individualMap.get(expectedCode);
                if (name == null) {
                    if (expectedPerson == null) {
                        continue;
                    }
                    //else
                    throw new RuntimeException(row.getNumber() + " Child #" + i + " " + chidrensNames.get(i) + " is null genCode:" +
                            expectedCode + " not sure why. person" + expectedPerson);
                }
                if (expectedPerson == null) {
                    System.out.println(row.getNumber() + " Child #" + i + " " + name.getLastCommaFirst() + " NOT FOUND under genCode:" +
                            expectedCode + " so we should build that person.");
                } else {
                    boolean success = PersonMerger.merge(expectedPerson, name, row.getNumber(), " Child #" + i);
                    if (!success) {
                        System.out.println(" Create new person? Or fix data?");
                    }
                }
            }

        }
        // get the kids names
        // for each kid
        // build the gencode of the kid and look up person
        // merge the name and person
    }

    static void addChild(Person parent, Row row, Map<String, Person> individualMap) {
        int num = GenCode.getChildNumber(row.getGenCode()); //num is base 1, not base 0;
        Person child = individualMap.get(GenCode.buildSelfCode(row.getGenCode()));
        //parent.addChild(child, num);
        if (parent.isMale()) {
            child.setFather(parent);
        } else {
            child.setMother(parent);
        }
    }

    public static void mergeInChildren(Row row, Map<String, Person> individualMap) {
        List<Name> names = extractChildrensNames(row);
        Person self = individualMap.get(row.getGenCode());
        if (self == null) {
            throw new RuntimeException("mergeInChildren self is null for ln#" + row.getNumber() +
                    " genCode:" + row.getGenCode() + " children:" + names);
        } else {
            int i = 1;    // "i" is Base 1. Not base 0
            for (Name name : names) {
                //mergeInChildren(i, name, self.getChild(i++), row);
            }
        }

    }

    public static List<Name> extractChildrensNames(Row row) {
        List<Name> names = new ArrayList<>();

        for (int i = 1; i <= Child.MAX_CHILDREN; i++) {
            Name name = Name.extractChildrensName(row.getChild(i));
            if (name != null) {
                names.add(name);
            }
        }
        return names;
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
            if (child != null && Name.areNamesPossiblyMisspelled(childsName, child.getName())) {
                System.out.println("Child POSSIBLE MISSPELLING. ln#:" + row.getNumber() +
                        "\n Child's name from row text:'" + childsName.getLastCommaFirst() + "' is SIMILAR to" +
                        "\n Child's name from gencode :'" + child.getName().getLastCommaFirst() + "' " + child.getGenCode());

            } else {
                System.out.println("Child list not match. ln#:" + row.getNumber() + " child#:" + i +
                        "\n Child's name from row text:'" + childsName.getLastCommaFirst() + "'" +
                        "\n Child's name from gencode :" + (child == null ? "null" : "'" + child.getName().getLastCommaFirst() + "' " + child.getGenCode()));
            }
        }
    }


}
