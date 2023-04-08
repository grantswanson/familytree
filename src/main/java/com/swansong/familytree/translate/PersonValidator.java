package com.swansong.familytree.translate;

import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;

public class PersonValidator {
    public static void validate(Person person) throws IllegalArgumentException {
        String personName = person.getName() != null ? person.getName().toNameKey().toString() : "Unknown";

        if (DateUtils.isBefore(person.getBaptismDate(), person.getDob())) {
            throw new IllegalArgumentException("Baptism date cannot be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Baptism date: " + person.getBaptismDate());
        }
        if (DateUtils.isBefore(person.getConfirmationDate(), person.getDob())) {
            throw new IllegalArgumentException("Confirmation date cannot be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Confirmation date: " + person.getConfirmationDate());
        }
        if (DateUtils.isBefore(person.getHighSchoolGradDate(), person.getDob())) {
            throw new IllegalArgumentException("HS Grad date cannot be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Confirmation date: " + person.getHighSchoolGradDate());
        }
        if (DateUtils.isBefore(person.getDeathDate(), person.getDob())) {
            throw new IllegalArgumentException("Death date cannot be before birth date for " + personName + ". Birth date: " + person.getDob() + ", Death date: " + person.getDeathDate());
        }


        if (DateUtils.isAfter(person.getBaptismDate(), person.getDeathDate())) {
            throw new IllegalArgumentException("Baptism date cannot be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Baptism date: " + person.getBaptismDate());
        }
        if (DateUtils.isAfter(person.getConfirmationDate(), person.getDeathDate())) {
            throw new IllegalArgumentException("Confirmation date cannot be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Confirmation date: " + person.getConfirmationDate());
        }
        if (DateUtils.isAfter(person.getHighSchoolGradDate(), person.getDeathDate())) {
            throw new IllegalArgumentException("HS Grad date cannot be after death date for " + personName + ". Death date: " + person.getDeathDate() + ", Confirmation date: " + person.getHighSchoolGradDate());
        }
    }

}
