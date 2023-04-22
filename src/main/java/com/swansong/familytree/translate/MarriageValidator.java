package com.swansong.familytree.translate;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.model.Source;
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

    public static void verifyKids(Marriage marriage) {
        int kidCount = marriage.getChildrenList().size();

        if (marriage.getSources().contains(Source.SpousesParents) && kidCount == 1) {
            return; // success
        }
        long parentCount = marriage.getSources().stream().filter(source -> source.equals(Source.Parents)).count();
        long fatherCount = marriage.getHusband() == null ? -1 :
                marriage.getHusband().getSources().stream().filter(source -> source.equals(Source.Parents)).count();
        long motherCount = marriage.getWife() == null ? -1 :
                marriage.getWife().getSources().stream().filter(source -> source.equals(Source.Parents)).count();

        if (parentCount != kidCount ||
                (fatherCount != kidCount && motherCount != kidCount)) {
            List<String> ignoreList = List.of(
                    "MAGA1E2", // ignore ln#455 Johnson, Jodie it is correct
                    "MABABC", //ignore ln#121 Kracht, Kelly Sue. She is correct.
                    "MAGA2a",//ignore ln#459 Saathoff, Rita Mae. She is correct (I think).
                    "MABCFE1",//ignore ln#199 Anliker, Jeff. He is correct
                    "MABHA1",//ignore ln#270 Covey, Jane Elaine Schafroth. She is correct (I think).
                    "HAABF1A1",
                    "MA1AAB"

            );
            if (marriage.getWife() == null || marriage.getHusband() == null || // it often gets messed up when a parent is missing... often it is kids from other marriages
                    ignoreList.contains(marriage.getWife().getGenCode()) ||
                    ignoreList.contains(marriage.getHusband().getGenCode())) {
                return;
            } // else
            throw new RuntimeException("#Kids not correct somewhere... " +
                    "\n # kids   :" + kidCount +
                    "\n parentCnt:" + parentCount +
                    "\n fatherCnt:" + fatherCount +
                    "\n motherCnt:" + motherCount +
                    "\n marriage:" + marriage
            );
        }
    }
}
