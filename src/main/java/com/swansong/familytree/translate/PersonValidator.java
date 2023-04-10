package com.swansong.familytree.translate;

import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonValidator {
    public static List<String> getWarnings(Person person) {
        String personName = person.getName() != null ? person.getName().toNameKey().toString() : "Unknown";
        List<String> warnings = new ArrayList<>();

        if (DateUtils.isBefore(person.getBaptismDate(), person.getDob())) {
            warnings.add("Baptism date should NOT be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Baptism date: " + person.getBaptismDate());
        }
        if (DateUtils.isBefore(person.getConfirmationDate(), person.getDob())) {
            warnings.add("Confirmation date should NOT be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Confirmation date: " + person.getConfirmationDate());
        }
        if (DateUtils.isBefore(person.getHighSchoolGradDate(), person.getDob())) {
            warnings.add("HS Grad date should NOT be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Confirmation date: " + person.getHighSchoolGradDate());
        }
        if (DateUtils.isBefore(person.getDeathDate(), person.getDob())) {
            warnings.add("Death date should NOT be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Death date: " + person.getDeathDate());
        }


        if (DateUtils.isAfter(person.getBaptismDate(), person.getDeathDate())) {
            warnings.add("Baptism date should NOT be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Baptism date: " + person.getBaptismDate());
        }
        if (DateUtils.isAfter(person.getConfirmationDate(), person.getDeathDate())) {
            warnings.add("Confirmation date should NOT be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Confirmation date: " + person.getConfirmationDate());
        }
        if (DateUtils.isAfter(person.getHighSchoolGradDate(), person.getDeathDate())) {
            warnings.add("HS Grad date should NOT be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Confirmation date: " + person.getHighSchoolGradDate());
        }
        return warnings;
    }

}
