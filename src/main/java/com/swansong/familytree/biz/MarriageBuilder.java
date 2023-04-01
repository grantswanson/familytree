package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.List;


public class MarriageBuilder {

    public static Marriage buildMarriage(Person mainPerson, Person spouse, List<Name> childrensNames, Row row) {

        Marriage marriage = new Marriage();
        marriage.setSpouse1(mainPerson);
        marriage.setSpouse2(spouse);
        marriage.setSourceLineNumber(row.getNumber());
        if (mainPerson != null) {
            mainPerson.addMarriage(marriage);
            mainPerson.addSpouse(spouse);
        }

        if (spouse != null) {
            spouse.addSpouse(mainPerson);
            spouse.addMarriage(marriage);
        }

        // add more here from row

        marriage.setChidrensNames(childrensNames);
        return marriage;
    }

}

