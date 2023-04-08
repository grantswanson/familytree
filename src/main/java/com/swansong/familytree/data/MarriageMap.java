package com.swansong.familytree.data;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.utils.StringUtils;
import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MarriageMap {
    public static Map<Integer, Marriage> marriages = new HashMap<>();

    public static void addMarriage(Marriage newMarriage) {
        Marriage currentMarriage = marriages.get(newMarriage.getId());
        if (currentMarriage != null) {
            throw new RuntimeException("Error: Tried to add someone who is already there by genCode! cur:" +
                    currentMarriage.toFormattedString() + "\n trying to add:" + newMarriage.toFormattedString() +
                    "\n diff:" + StringUtils.diff(currentMarriage.toString(), newMarriage.toString()));
        }
        marriages.put(newMarriage.getId(), newMarriage);
    }

    public static Collection<Marriage> getMarriagesCollection() {
//        Comparator<Marriage> valueComparator = Comparator
//                .comparing((Marriage e) -> e.getHusband().getName().toFullName());
//        return marriages.values().stream().sorted(valueComparator).collect(Collectors.toList());
        return marriages.values().stream().sorted(Comparator.comparingInt(Marriage::getId)).collect(Collectors.toList());
    }

    public static void printMarriages() {
        System.out.println("\nMarriages...");
        Collection<Marriage> marriages = getMarriagesCollection();
        // build the marriages
        for (Marriage marriage : marriages) {

            String str = marriage.toFormattedString();

            System.out.println(str);
        }
        System.out.println("Marriage Count:" + marriages.size());

    }

    public static int count() {
        return marriages.size();
    }

}
