package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;

import java.util.Map;


public class MarriageBuilder {

    public static Marriage buildMarriage(Person mainPerson, Person spouse, Row row) {
        Marriage marriage = new Marriage();
        marriage.setHusband(mainPerson);
        marriage.setWife(spouse);
        marriage.setSourceLineNumber(row.getNumber());

        // add more here from row

        return marriage;
    }

    public static Marriage buildMarriage(Row row, Map<String, Person> individualMap) {
        // find existing parents
        Person originalParent = individualMap.get(GenCode.buildOriginalParentsCode(row.getGenCode()));
        Person spouseParent = individualMap.get(GenCode.buildSpousesParentsCode(row.getGenCode()));

        //Map<String, Person> origSpouses = originalParent.getSpouses();
        //Map<String, Person> spousesSpouses = spouseParent.getSpouses();

        //Person father = PersonBuilder.buildFather(row);
        //Person mother = PersonBuilder.buildMother(row);

//        boolean isMale=true;
//        Map.Entry<PersonMerger.MergeResults, Person> results =
//                 PersonMerger.merge(originalParent, father, row, individualMap,isMale );
//
//        isMale=false;
//        Map.Entry<PersonMerger.MergeResults, Person> results =
//                mother = PersonMerger.merge(originalParent, mother, row, individualMap,isMale );

        Marriage marriage = new Marriage();

        return marriage;
    }



}

