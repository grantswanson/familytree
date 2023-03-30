package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;


public class MarriageBuilder {

    public static Marriage buildMarriage(Person mainPerson, Person spouse, Row row) {
        Marriage marriage = new Marriage();
        marriage.setSpouse1(mainPerson);
        marriage.setSpouse2(spouse);
        marriage.setSourceLineNumber(row.getNumber());

        // add more here from row

        return marriage;
    }

}

