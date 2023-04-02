package com.swansong.familytree.model;

import com.swansong.familytree.csv.Row;
import lombok.Data;
import lombok.ToString;

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
    private Row sourceRow;

//    @ToString.Exclude
//    // just the names from the row
//    private List<Name> chidrensNames = new ArrayList<>();

//    public String childrensNamesToString() {
//        StringBuilder strBuilder = new StringBuilder();
//        for (Name child : chidrensNames) {
//            if (child == null) {
//                continue;
//            }
//            strBuilder.append(String.format("%s", child.getFirstNames() + ", "));
//
//        }
//        if (strBuilder.length() != 0) {
//            strBuilder.insert(0, "  KidsNames:");
//        }
//        return strBuilder.toString();
//    }

    @ToString.Exclude
    private Person[] children = new Person[Child.MAX_CHILDREN];

    @ToString.Include
    public String childrenToString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Person child : children) {
            if (child == null) {
                continue;
            }
            strBuilder.append(String.format("#%d %s %s", child.getSourceLineNumber(), child.getGenCode(),
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
        Child.verifyChildNumber(childNum);
        if (children[childNum - 1] != null) {
            System.out.println(
                    "overwriting existing child:" + children[childNum - 1].getGenCode() + " " + children[childNum - 1].getName().getLastCommaFirst() +
                            "\n            with new child:" + child.getGenCode() + " " + child.getName().getLastCommaFirst());
        }
        children[childNum - 1] = child;
    }

    /**
     * @param i Base 1. Not base 0
     */
    @SuppressWarnings("unused")
    public Person getChild(int i) {
        Child.verifyChildNumber(i);
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

//    public boolean isSpousesParents() {
//        // if there is only one child then expect it to be the spouse because at this point in the code,
//        // the children are not filled in
//        boolean onlyOneChild = children[0] != null &&
//                IntStream.range(1, children.length).allMatch(i -> children[i] == null);
//
//        boolean spousesParentsNamesMatch = Name.parseLastCommaFirstName(sourceRow.getSpouseFather()).getLastCommaFirst().equalsIgnoreCase(
//                getHusband().getName().getLastCommaFirst()) ||
//                Name.parseLastCommaFirstName(sourceRow.getSpouseMother()).getLastCommaFirst().equalsIgnoreCase(
//                        getWife().getName().getLastCommaFirst());
//        if(onlyOneChild != spousesParentsNamesMatch) {
//            throw new RuntimeException("OnlyOncChild:"+onlyOneChild +
//                    " fails to match spousesParentsNamesMatch:"+spousesParentsNamesMatch+
//                    " marriage:"+this);
//        }
//        else { return spousesParentsNamesMatch;}
//    }
}
