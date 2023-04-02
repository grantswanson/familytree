package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Child;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OldChildBuilder {
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
}
