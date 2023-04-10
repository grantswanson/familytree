package com.swansong.familytree.translate;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MarriageValidator {
    public static List<String> getWarnings(Marriage marriage) {
        List<String> warnings = new ArrayList<>();

        String husbandName = getHusbandsName(marriage);
        String wifeName = getWifesName(marriage);

        if (DateUtils.isBefore(marriage.getDivorceDate(), marriage.getMarriageDate())) {
            warnings.add(
                    "Divorce date should NOT be before marriage date for " +
                            husbandName + " and " + wifeName + ". Marriage date: " + marriage.getMarriageDate() +
                            ", Divorce date: " + marriage.getDivorceDate());
        }


        if (marriage.getHusband() != null && DateUtils.isBefore(marriage.getMarriageDate(), marriage.getHusband().getDob())) {
            warnings.add("Marriage date should NOT be before birth date for " + husbandName +
                    ". Birth date: " + marriage.getHusband().getDob() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }

        if (marriage.getHusband() != null && DateUtils.isAfter(marriage.getMarriageDate(), marriage.getHusband().getDeathDate())) {
            warnings.add("Marriage date should NOT be after death date for " + husbandName +
                    ". Death date: " + marriage.getHusband().getDeathDate() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }


        if (marriage.getWife() != null && DateUtils.isBefore(marriage.getMarriageDate(), marriage.getWife().getDob())) {
            warnings.add("Marriage date should NOT be before birth date for " + wifeName +
                    ". Birth date: " + marriage.getWife().getDob() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }

        if (marriage.getWife() != null && DateUtils.isAfter(marriage.getMarriageDate(), marriage.getWife().getDeathDate())) {
            warnings.add("Marriage date should NOT be after death date for " + wifeName +
                    ". Death date: " + marriage.getWife().getDeathDate() +
                    ", Marriage date: " + marriage.getMarriageDate());
        }
        return warnings;
    }

    private static String getWifesName(Marriage marriage) {
        String wifeName = "Unknown";
        if (marriage.getWife() != null && marriage.getWife().getName() != null) {
            wifeName = marriage.getWife().getName().toNameKey().toString();
        }
        return wifeName;
    }

    private static String getHusbandsName(Marriage marriage) {
        String husbandName = "Unknown";
        if (marriage.getHusband() != null && marriage.getHusband().getName() != null) {
            husbandName = marriage.getHusband().getName().toNameKey().toString();
        }
        return husbandName;
    }


    public static void validate(Marriage marriage) throws IllegalArgumentException {
        String husbandName = getHusbandsName(marriage);
        String wifeName = getWifesName(marriage);

        if (marriage.getHusband() != null) {
            for (Person child : marriage.getChildrenList()) {
                if (marriage.getHusband().getId() == child.getId()) {
                    throw new IllegalArgumentException("Parents shouldn't be children in the same family. Husband:" + husbandName +
                            ". childName: " + child.getName().toFullName() +
                            " ln#:" + marriage.getSourceRow().getNumber());
                }
            }
        }

        if (marriage.getWife() != null) {
            for (Person child : marriage.getChildrenList()) {
                if (marriage.getWife().getId() == child.getId()) {
                    throw new IllegalArgumentException("Parents shouldn't children in the same family. Wife:" + wifeName +
                            ". childName: " + child.getName().toFullName() +
                            " ln#:" + marriage.getSourceRow().getNumber());
                }
            }
        }

    }
}
