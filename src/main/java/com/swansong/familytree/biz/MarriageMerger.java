package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.MarriageSource;
import com.swansong.familytree.model.Person;

public class MarriageMerger {

    public static void verifyExistingMarriage(Person husband, Person wife, MarriageSource source, Person child, Row row) {
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
            if (source == MarriageSource.SpousesParents) {
                marriage.addChild(child, 1);
            }
            MarriageMap.addMarriage(marriage);

            if (husband != null && wife != null) {
                System.out.println("Warn: Expected marriage to already be there. People exist. Built new one: ln#" + row.getNumber() +
                        "\n husband:" + (husband != null ? husband.toFormattedString() : "none") + // TODO look at this. Always true.
                        "\n    wife:" + (wife != null ? wife.toFormattedString() : "none") +
                        " source:" + source +
                        "\n newMarriage:" + marriage.toFormattedString());
            }

        }
    }
}
