package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.List;
import java.util.Map;

public class Verifier {
    public static void mergeInChildren(Row row, Map<String, Person> individualMap) {

        List<Name> names = PersonBuilder.extractChildrensNames(row);
        Person self = individualMap.get(row.getGenCode());
        int i = 0;
        for (Name name : names) {
            mergeInChildren(i, name, self.getChild(i++), row, individualMap);
        }

    }

    private static void mergeInChildren(int i, Name childsName, Person child, Row row, Map<String, Person> individualMap) {
        if (childsName.isBlank()) {
            return;
        }
        if (child != null && Name.isMergeAllowed(childsName, child.getName())) {
            child.getName().mergeInName(childsName);
        } else {
            System.out.println("Child list not match. ln#:" + row.getNumber() + " child#:" + i +
                    "\n childsname (from row of self):" + childsName.getLastCommaFirst() +
                    "\n Child:" + (child == null ? "null" : child.getName().getLastCommaFirst()));
        }
    }
}
