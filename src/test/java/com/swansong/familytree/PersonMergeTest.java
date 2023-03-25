package com.swansong.familytree;

import com.swansong.familytree.biz.PersonMerger;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonMergeTest {

    @Test
    void testPersonMerge() {
//        Person existingPerson = null;
//        Person newPerson = null;
//        PersonMerger.MergeResults results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_NOBODY, results);
//
//        existingPerson = new Person();
//        newPerson = null;
//        results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_EXISTING, results);
//
//        existingPerson = null;
//        newPerson = new Person();
//        results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_NEW, results);
//
//        existingPerson = new Person();
//        existingPerson.setGenCode("SABC");
//        existingPerson.setName(Name.parseLastCommaFirstName("Hudson, Paige"));
//        newPerson = new Person();
//        newPerson.setGenCode("SABC");
//        newPerson.setName(Name.parseLastCommaFirstName("Hudson, Paige \"Paigie\" [Hudson]"));
//        results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_EXISTING, results);
//
//        existingPerson = new Person();
//        existingPerson.setGenCode("SABC");
//        existingPerson.setName(Name.parseLastCommaFirstName("Swanson, Grant"));
//        newPerson = new Person();
//        newPerson.setGenCode("SABC");
//        newPerson.setName(Name.parseLastCommaFirstName("Swanson, Grant, Jr."));
//        results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_EXISTING, results);
//
//        existingPerson = new Person();
//        existingPerson.setGenCode("SABC");
//        existingPerson.setName(Name.parseLastCommaFirstName("Swanson, Grant"));
//        newPerson = new Person();
//        newPerson.setGenCode("SAB");
//        newPerson.setName(Name.parseLastCommaFirstName("Swanson, Grant"));
//        results =  PersonMerger.merge(existingPerson, newPerson );
//        assertEquals(PersonMerger.MergeResults.USE_EXISTING, results);
    }
}
