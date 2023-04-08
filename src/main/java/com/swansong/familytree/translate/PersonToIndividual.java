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
        individual.setBirthDate(DateUtils.convertDate(person.getDob()));
        individual.setBirthPlace(person.getPob());
        individual.setBaptismDate(DateUtils.convertDate(person.getBaptismDate()));
        individual.setBaptismPlace(person.getBaptismPlace());
        individual.setConfirmationDate(DateUtils.convertDate(person.getConfirmationDate()));
        individual.setConfirmationPlace(person.getConfirmationPlace());
        individual.setHighSchoolGraduationDate(person.getHighSchoolGradDate());
        individual.setHighSchoolGraduationPlace(person.getHighSchoolGradPlace());
        individual.setDeathDate(DateUtils.convertDate(person.getDeathDate()));
        individual.setDeathPlace(person.getBurialPlace());

        individual.setOccupation(person.getOccupation());
        individual.setNotes(person.getNotes());
        if (person.getChildrenNotes() != null && !person.getChildrenNotes().isBlank()) {
            individual.addNote(person.getChildrenNotes());
        }
        return individual;
    }


}