package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.model.Source;

public class MarriageMerger {

    public static void verifyExistingMarriage(Person husband, Person wife, Source source, Person child, Row row) {
        //if the marriage exists, just use it
        Marriage existingMarriage = MarriageMap.findMarriage(husband, wife);
        if (existingMarriage != null) {
//            System.out.println("Info: Found existing marriage for 2 existing people ln#" + row.getNumber() +
//                    "\n current:" + existingMarriage.toFormattedString() +
//                    "\n husband:" + (husband != null ? husband.toFormattedString() : "none") +
//                    "\n    wife:" + (wife != null ? wife.toFormattedString() : "none") +
//                    " source:" + source);

            // TODO verify parents are correct and child is one of the children

        } else {
            Marriage marriage = MarriageBuilder.buildMarriage(husband, wife, row, source);
            if (source == Source.SpousesParents) {
                marriage.addChild(child, 1);
                marriage.addSource(Source.SpousesParents);
            }
            MarriageMap.addMarriage(marriage);

            if (husband != null && wife != null) {
                System.out.println("Warn: Expected marriage to already be there. People exist. Built new one: ln#" + row.getNumber() +
                        "\n husband:" + husband.toFormattedString() + // TODO look at this. Always true.
                        "\n    wife:" + wife.toFormattedString() +
                        " source:" + source +
                        "\n newMarriage:" + marriage.toFormattedString());
            }

        }
    }
}
