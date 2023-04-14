package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.model.Source;


public class MarriageBuilder {

    public static Marriage buildMarriage(Person mainPerson, Person spouse, Row row, Source source) {

        Marriage marriage = new Marriage();
        marriage.setSpouse1(mainPerson);
        marriage.setSpouse2(spouse);
        marriage.setSource(source);
        marriage.setSourceRow(row);
        if (mainPerson != null) {
            mainPerson.addMarriage(marriage);
            mainPerson.addSpouse(spouse);
        }

        if (spouse != null) {
            spouse.addSpouse(mainPerson);
            spouse.addMarriage(marriage);
        }

        return marriage;
    }

    public static void addRowDetails(Marriage marriage, Row row) {

        // add more here from row
        marriage.setMarriageDate(row.getMarriageDate());
        marriage.setMarriagePlace(row.getMarriagePlace());
        marriage.setDivorceDate(row.getDivorceDate());
        marriage.setDivorcePlace(row.getDivorcePlace());
        marriage.addNote(row.getChildrenNotes());

        // don't add the children yet. However, the names are in the row
    }

}

