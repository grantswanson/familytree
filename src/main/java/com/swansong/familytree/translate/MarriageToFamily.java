package com.swansong.familytree.translate;

import com.swansong.familytree.gedcom.Family;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;

public class MarriageToFamily {
    public static Family convert(Marriage marriage) {
        MarriageValidator.validate(marriage);

        Family family = new Family();
        family.setId(marriage.getId());
        if (marriage.getHusband() != null) {
            family.setHusbandId(marriage.getHusband().getId());
        }
        if (marriage.getWife() != null) {
            family.setWifeId(marriage.getWife().getId());
        }

        for (Person child : marriage.getChildren()) {
            if (child != null) {
                family.getChildIds().add(child.getId());
            }
        }

        family.setMarriageDate(DateUtils.convertDate(marriage.getMarriageDate()));
        family.setMarriagePlace(marriage.getMarriagePlace());
        family.setDivorceDate(DateUtils.convertDate(marriage.getDivorceDate()));
        family.setDivorcePlace(marriage.getDivorcePlace());

        family.setNotes(marriage.getNotes());

        for (Person child : marriage.getChildrenFromUnRelatedMarriage()) {
            if (child != null) {
                family.addNote("Technically unrelated child: " + child.getName().toNameKey().toString());
            }
        }

        return family;
    }
}
