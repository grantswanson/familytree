package com.swansong.familytree.translate;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.utils.DateUtils;

public class MarriageValidator {
    public static void validate(Marriage marriage) throws IllegalArgumentException {
        String husbandName = "Unknown";
        String wifeName = "Unknown";

        if (marriage.getHusband() != null && marriage.getHusband().getName() != null) {
            husbandName = marriage.getHusband().getName().toNameKey().toString();
        }
        if (marriage.getWife() != null && marriage.getWife().getName() != null) {
            wifeName = marriage.getWife().getName().toNameKey().toString();
        }

        if (DateUtils.isBefore(marriage.getDivorceDate(), marriage.getMarriageDate())) {
            throw new IllegalArgumentException("Divorce date cannot be before marriage date for " +
                    husbandName + " and " + wifeName + ". Marriage date: " + marriage.getMarriageDate() +
                    ", Divorce date: " + marriage.getDivorceDate());
        }


        if (marriage.getHusband() != null && DateUtils.isBefore(marriage.getMarriageDate(), marriage.getHusband().getDob())) {
            throw new IllegalArgumentException("Marriage date cannot be before birth date for " + husbandName +
                    ". Birth date: " + marriage.getHusband().getDob() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }

        if (marriage.getHusband() != null && DateUtils.isAfter(marriage.getMarriageDate(), marriage.getHusband().getDeathDate())) {
            throw new IllegalArgumentException("Marriage date cannot be after death date for " + husbandName +
                    ". Death date: " + marriage.getHusband().getDeathDate() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }


        if (marriage.getWife() != null && DateUtils.isBefore(marriage.getMarriageDate(), marriage.getWife().getDob())) {
            throw new IllegalArgumentException("Marriage date cannot be before birth date for " + wifeName +
                    ". Birth date: " + marriage.getWife().getDob() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }

        if (marriage.getWife() != null && DateUtils.isAfter(marriage.getMarriageDate(), marriage.getWife().getDeathDate())) {
            throw new IllegalArgumentException("Marriage date cannot be after death date for " + wifeName +
                    ". Death date: " + marriage.getWife().getDeathDate() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }

    }
}
