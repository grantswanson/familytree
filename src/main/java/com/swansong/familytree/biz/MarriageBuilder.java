package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;


public class MarriageBuilder {

    public static Marriage buildMarriage(Person mainPerson, Person spouse, Row row) {

        Marriage marriage = new Marriage();
        marriage.setSpouse1(mainPerson);
        marriage.setSpouse2(spouse);
        marriage.setSourceRow(row);
        if (mainPerson != null) {
            mainPerson.addMarriage(marriage);
            mainPerson.addSpouse(spouse);
        }

        if (spouse != null) {
            spouse.addSpouse(mainPerson);
            spouse.addMarriage(marriage);
        }
        // add more here from row
        marriage.setMarriageDate(row.getMarriageDate());
        marriage.setMarriagePlace(row.getMarriagePlace());
        marriage.setDivorceDate(row.getDivorceDate());
        marriage.setDivorcePlace(row.getDivorcePlace());
        marriage.setChildNotes(row.getChildrenNotes());

        // don't add the children yet. However, the names are in the row
        return marriage;
    }

}

