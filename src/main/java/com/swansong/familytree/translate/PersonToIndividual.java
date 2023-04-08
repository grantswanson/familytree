package com.swansong.familytree.translate;

import com.swansong.familytree.gedcom.Individual;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;

public class PersonToIndividual {
    public static Individual convertPersonToIndividual(Person person) {
        PersonValidator.validatePersonDates(person);

        Individual individual = new Individual();
        individual.setId(person.getId());
        if (person.getName() != null) {
            individual.setGivenName(person.getName().getFirstNames()); // TODO nicknames, alt names, married names
            individual.setSurname(person.getName().getSurName());
        }
        individual.setBirthDate(convertDate(person.getDob()));
        individual.setBirthPlace(person.getPob());
        individual.setDeathDate(convertDate(person.getDeathDate()));
        individual.setDeathPlace(person.getBurialPlace());
        individual.setConfirmationDate(convertDate(person.getConfirmationDate()));
        individual.setConfirmationPlace(person.getConfirmationPlace());
        individual.setHighSchoolGraduationDate(person.getHighSchoolGradDate());
        individual.setHighSchoolGraduationPlace(person.getHighSchoolGradPlace());
        individual.setOccupation(person.getOccupation());
        individual.setNotes(person.getNotes());
        if (person.getChildrenNotes() != null && !person.getChildrenNotes().isBlank()) {
            individual.addNote(person.getChildrenNotes());
        }
        return individual;
    }

    private static String convertDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        String[] dateParts = date.split(" ");
        if (dateParts.length == 3) {
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            if (DateUtils.isValidDate(year, month, day)) {
                return day + " " + month + " " + year;
            } else {
                return "";
            }
        } else if (dateParts.length == 2) {
            String year = dateParts[0];
            String month = dateParts[1];
            if (DateUtils.isValidYear(year)) {
                return month + " " + year;
            } else {
                return "";
            }
        } else if (dateParts.length == 1) {
            String year = dateParts[0];
            if (DateUtils.isValidYear(year)) {
                return year;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


}