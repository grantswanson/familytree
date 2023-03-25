package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;

import java.util.Map;


public class MarriageBuilder {

    public static Marriage buildMarriage(Row row, Map<String, Person> individualMap) {
        // find existing parents
        Person existingParent = individualMap.get(GenCode.buildParentsIndividualCode(row.getGenCode()));

        Person father = PersonBuilder.buildFather(row);
        Person mother = PersonBuilder.buildMother(row);
        //Person spouse = existingParent.getSpouse();

//        boolean isMale=true;
//        Map.Entry<PersonMerger.MergeResults, Person> results =
//                 PersonMerger.merge(existingParent, father, row, individualMap,isMale );
//
//        isMale=false;
//        Map.Entry<PersonMerger.MergeResults, Person> results =
//                mother = PersonMerger.merge(existingParent, mother, row, individualMap,isMale );

        Marriage marriage = new Marriage();

        return marriage;
    }



}

