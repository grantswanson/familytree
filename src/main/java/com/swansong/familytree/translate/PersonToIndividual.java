package com.swansong.familytree.translate;

import com.swansong.familytree.gedcom.Individual;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;
import com.swansong.familytree.utils.StringUtils;

public class PersonToIndividual {
    public static Individual convertPersonToIndividual(Person person) {

        Individual individual = new Individual();
        individual.setId(person.getId());
        if (person.getName() != null) {

            individual.setGivenName(removeLastNumber(person.getName().getFirstNames()));
            individual.setSurName(person.getName().getSurName());
            individual.setNickName(person.getName().getNickName());
            individual.setSuffix(person.getName().getSuffix());
            individual.setAliasNames(person.getName().getAltNames());
            for (String marriedName : person.getName().getMarriedNames()) {
                individual.addNote("Married name: " + marriedName);
            }
        }
        individual.setReferenceNumber("CustomGenCode:" + person.getGenCode());
        individual.setGender(person.getGender());
        individual.setBirthDate(DateUtils.convertDate(person.getDob()));
        individual.setBirthPlace(person.getPob());
        individual.setBaptismDate(DateUtils.convertDate(person.getBaptismDate()));
        individual.setBaptismPlace(person.getBaptismPlace());
        individual.setConfirmationDate(DateUtils.convertDate(person.getConfirmationDate()));
        individual.setConfirmationPlace(person.getConfirmationPlace());
        individual.setHighSchoolGraduationDate(DateUtils.convertDate(person.getHighSchoolGradDate()));
        individual.setHighSchoolGraduationPlace(person.getHighSchoolGradPlace());
        individual.setDeathDate(DateUtils.convertDate(person.getDeathDate()));
        individual.setDeathPlace(person.getBurialPlace());

        person.getMarriages().forEach(marriage -> individual.addMarriageId(marriage.getId()));
        if (person.getParentsMarriage() != null) {
            individual.setParentsMarriageId(person.getParentsMarriage().getId());
        }
        individual.setOccupation(person.getOccupation());
        individual.setNotes(person.getNotes());
        if (person.getChildrenNotes() != null && !person.getChildrenNotes().isBlank()) {
            individual.addNote(person.getChildrenNotes());
        }
        person.getProcessingNotes().forEach(individual::addNote);
        PersonValidator.getWarnings(person).forEach(individual::addNote);

        return individual;
    }

    private static String removeLastNumber(String givenName) {
        String s = StringUtils.removeLastNumber(givenName);
        if (!s.equals(givenName)) {
            System.out.println("Changing name from:" + givenName + " to:" + s);
        }
        return s;
    }

}