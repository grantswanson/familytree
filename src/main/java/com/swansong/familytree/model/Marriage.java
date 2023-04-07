package com.swansong.familytree.model;

import com.swansong.familytree.StringUtilities;
import com.swansong.familytree.biz.ChildBuilder;
import com.swansong.familytree.csv.Row;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class Marriage {

    private final static String defaultSource = "Donovan Burdette Meyer";

    public Marriage() {
        id = Id.MARRIAGE.nextId();
    }

    private String id;
    private Person spouse1;
    private Person spouse2;
    private boolean isSpousesParents;


    @ToString.Exclude
    private Person[] children = new Person[ChildBuilder.MAX_CHILDREN];
    private List<Person> childrenFromUnRelatedMarriage = new ArrayList<>();

    @ToString.Exclude
    private Row sourceRow;

    @ToString.Include
    public String childrenToString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Person child : children) {
            if (child == null) {
                continue;
            }
            strBuilder.append(String.format("#%d %s %s", child.getSourceRow().getNumber(), child.getGenCode(),
                    child.getName().getFirstNames() + ", "));

        }
        if (strBuilder.length() != 0) {
            strBuilder.insert(0, "  Kids:");
        }
        return strBuilder.toString();
    }

    /**
     * @param childNum Base 1. Not base 0
     */
    public void addChild(Person child, int childNum) {
        ChildBuilder.verifyChildNumber(childNum);
        Person existingChild = children[childNum - 1];
        if (existingChild != null &&
                StringUtilities.diff(existingChild.toString(), child.toString()).size() > 0) {
            System.out.println(
                    "overwriting existing child #:" + childNum + " " + existingChild.getGenCode() + " " + existingChild.getName().toFullName() +
                            "\n               with new child:" + child.getGenCode() + " " + child.getName().toFullName() +
                            "\n existing:" + existingChild +
                            "\n      new:" + child +
                            "\n differences:" + StringUtilities.diff(existingChild.toString(), child.toString()));
        }
        children[childNum - 1] = child;
    }

    /**
     * @param i Base 1. Not base 0
     */
    @SuppressWarnings("unused")
    public Person getChild(int i) {
        ChildBuilder.verifyChildNumber(i);
        return children[i - 1];
    }


    public Person getHusband() {
        if (getSpouseNumOfHusband() == 1) {
            return spouse1;
        } else {
            return spouse2;
        }
    }

    private int getSpouseNumOfHusband() {
        if (spouse2 != null && spouse2.isMale()) {
            return 2;
        } else if (spouse1 != null && spouse1.isMale()) {
            return 1;
        } else { // just default to spouse 1
            return 1;
        }
    }

    public Person getWife() {
        // do the opposite of husband
        if (getSpouseNumOfHusband() == 1) {
            return spouse2;
        } else {
            return spouse1;
        }
    }

    public String getChildrensSurName() {
        if (spouse2 != null && spouse2.isMale()) {
            return spouse2.getName().getSurName();
        } else if (spouse1 != null && spouse1.isMale()) {
            return spouse1.getName().getSurName();
        } else { // only return if we know it
            return null;
        }
    }

    public void addChildFromUnRelatedMarriage(Person unRelatedChild) {
        childrenFromUnRelatedMarriage.add(unRelatedChild);
    }
}
