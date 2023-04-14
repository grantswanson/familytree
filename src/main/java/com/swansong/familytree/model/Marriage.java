package com.swansong.familytree.model;

import com.swansong.familytree.biz.ChildBuilder;
import com.swansong.familytree.csv.Row;
import com.swansong.familytree.utils.StringUtils;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Marriage {

    public Marriage() {
        id = Id.MARRIAGE.nextId();
    }

    private int id;

    @SuppressWarnings("unused")
    @ToString.Include
    private int lineNum() {
        return sourceRow.getNumber();
    }

    @ToString.Include
    public String getKey() {
        return getParentsGenCode(getHusband(), getWife());
    }

    public String getKeyReversed() {
        return getParentsGenCode(getWife(), getHusband());
    }

    @ToString.Exclude
    private Person spouse1;
    @ToString.Exclude
    private Person spouse2;
    private List<Source> sources = new ArrayList<>();

    public void addSource(Source s) {
        sources.add(s);
    }

    @ToString.Include
    public String sourcesToString() {
        String s = sources.stream()
                .filter(source -> !source.equals(Source.Parents))
                .map(Source::toString)
                .collect(Collectors.joining());
        long parentsCount = sources.stream()
                .filter(source -> source.equals(Source.Parents))
                .count();
        if (parentsCount > 0) {
            s += Source.Parents.toString() + parentsCount;
        }
        return s;
    }

    public void verifyKids() {
        int kidCount = getChildrenList().size();

        if (sources.contains(Source.SpousesParents) && kidCount == 1) {
            return; // success
        }
        long parentCount = sources.stream().filter(source -> source.equals(Source.Parents)).count();
        long fatherCount = getHusband().getSources().stream().filter(source -> source.equals(Source.Parents)).count();
        long motherCount = getWife() != null ?
                getWife().getSources().stream().filter(source -> source.equals(Source.Parents)).count() :
                -1;

        if (parentCount != kidCount ||
                (fatherCount != kidCount &&
                        motherCount != kidCount)) {
            throw new RuntimeException("#Kids not correct somewhere... " +
                    "\n # kids   :" + kidCount +
                    "\n parentCnt:" + parentCount +
                    "\n fatherCnt:" + fatherCount +
                    "\n motherCnt:" + motherCount +
                    "\n marriage:" + this
            );
        }
    }

    private String marriageDate = "";
    private String marriagePlace = "";

    private String divorceDate = "";
    private String divorcePlace = "";

    private List<String> notes = new ArrayList<>();

    public void addNote(String note) {
        notes.add(note);
    }

    @ToString.Exclude
    private Person[] children = new Person[ChildBuilder.MAX_CHILDREN];

    @ToString.Exclude
    private List<Person> childrenFromUnRelatedMarriage = new ArrayList<>();

    @ToString.Exclude
    private Row sourceRow;


    @ToString.Include
    public String childrenToString() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("  Kids(%d):", getChildrenList().size()));
        for (Person child : getChildrenList()) {
            strBuilder.append(formattedChildString(child));
        }

        String kids = strBuilder.toString();
        strBuilder = new StringBuilder();
        for (Person child : getChildrenFromUnRelatedMarriage()) {
            strBuilder.append(formattedChildString(child));
        }
        if (strBuilder.length() != 0) {
            strBuilder.insert(0,
                    String.format("  Unrelated Kids(%d):", getChildrenFromUnRelatedMarriage().size()));
        }
        return kids + strBuilder;
    }

    private static String formattedChildString(Person child) {
        return String.format("#%d %s %s", child.getSourceRow().getNumber(), child.getGenCode(),
                child.getName().getFirstNames() + ", ");
    }

    public List<Person> getChildrenList() {
        List<Person> c = new ArrayList<>();
        for (Person child : children) {
            if (child != null) {
                c.add(child);
            }
        }
        return c;
    }

    /**
     * @param childNum Base 1. Not base 0
     */
    public void addChild(Person child, int childNum) {
        ChildBuilder.verifyChildNumber(childNum);
        Person existingChild = children[childNum - 1];
        if (existingChild != null &&
                StringUtils.diff(existingChild.toString(), child.toString()).size() > 0) {
            System.out.println(
                    "overwriting existing child #:" + childNum + " " + existingChild.getGenCode() + " " + existingChild.getName().toFullName() +
                            "\n               with new child:" + child.getGenCode() + " " + child.getName().toFullName() +
                            "\n existing:" + existingChild +
                            "\n      new:" + child +
                            "\n differences:" + StringUtils.diff(existingChild.toString(), child.toString()));
        }
        children[childNum - 1] = child;
        if (child != null) {
            if (child.getParentsMarriage() != null && child.getParentsMarriage().getId() != id) {
                System.out.println("Warning: addChild() causing child to be associated with a different marriage. child:" + child.toShortString() +
                        "\n   old:" + child.getParentsMarriage().toFormattedString() +
                        "\n   new:" + this.toFormattedString());
            }
            child.setParentsMarriage(this);
        }
        if (sources.contains(Source.SpousesParents)) {
            if (child == null) {
                throw new RuntimeException("Spouse parents exist, but spouse does not. Probably a data mistake. Marriage ln#" + sourceRow.getNumber());
            }
        }
    }

    public void addChild(Person child) {
        int childIdx = 0;
        while (children[childIdx] != null) {
            childIdx++;
        }
        addChild(child, childIdx + 1);
    }

    /**
     * @param i Base 1. Not base 0
     */
    @SuppressWarnings("unused")
    public Person getChild(int i) {
        ChildBuilder.verifyChildNumber(i);
        return children[i - 1];
    }

    public static String getParentsGenCode(Person husband, Person wife) {
        String retVal = "";
        if (husband != null) {
            retVal = husband.getGenCode();
        }
        retVal += "+";
        if (wife != null) {
            retVal += wife.getGenCode();
        }
        return retVal;
    }

    @ToString.Include
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

    @ToString.Include
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


    @SuppressWarnings("unused")
    public String toFormattedString2() {
        String str = "";
        Person person = getHusband();
        if (person != null) {
            str += "\"" + person.getName().toFullName() + "\"";
        }

        person = getWife();
        if (person != null) {
            str += ", \"" + person.getName().toFullName() + "\"";
        }
        //str += String.format(" #%-2d M%2s ",
        //        getSourceRow().getNumber(), getId());
        //str += childrenToString();
        return str;
    }

    public String toFormattedString() {
        String str = String.format("#%-2d %4s ",
                getSourceRow().getNumber(), sourcesToString());
        Person person = getHusband();
        if (person != null) {
            str += " " + person.toShortString();
        }

        person = getWife();
        if (person != null) {
            str += " " + person.toShortString();
        } else {
            str += " unknown spouse                ";
        }
        str += childrenToString();
        return str;
    }

}
