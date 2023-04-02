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
                Person expectedPerson = individualMap.get(expectedCode);

                if (name == null && expectedPerson == null) {
                    continue;
                } else if (name == null) {
                    throw new RuntimeException("ln#" + row.getNumber() + " Child #" + i + " " + chidrensNames.get(i) + " is null. genCode:" +
                            expectedCode + " not sure why. person" + expectedPerson);
                } else if (expectedPerson == null) {
                    expectedCode = GenCode.buildNotRelatedChildsCode(row.getGenCode(), i + 1);
                    expectedPerson = individualMap.get(expectedCode);

//                    if (expectedPerson != null) {
//                        System.out.println("ln#"+row.getNumber() + " Child #" + i + " " + name.getLastCommaFirst() + " SUCCESS! FOUND under genCode:" +
//                                expectedCode + " so we should merge that person.");
//                    }
                }

                if (expectedPerson != null) {
                    boolean success = PersonMerger.merge(expectedPerson, name, row.getNumber(), " Child #" + i);
                    if (success) {
                        marriage.addChild(expectedPerson, i + 1);
                    } else {
                        System.out.println(" Merge failed!!! Fix data.");
                    }
                } else {
                    System.out.println("ln#" + row.getNumber() + " Child #" + i + " " + name.getLastCommaFirst() + " NOT FOUND under genCode:" +
                            expectedCode + " so we should build that person.");
                }
            }

        }
        // get the kids names
        // for each kid
        // build the gencode of the kid and look up person
        // merge the name and person
    }


}
