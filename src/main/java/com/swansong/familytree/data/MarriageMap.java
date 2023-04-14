package com.swansong.familytree.data;

import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Person;
import com.swansong.familytree.utils.StringUtils;
import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MarriageMap {
    private static Map<String, Marriage> marriages = new HashMap<>();

    public static Marriage findMarriage(Person husband, Person wife) {
        String key = Marriage.getParentsGenCode(husband, wife);
        Marriage marriage = marriages.get(key);
        if (marriage == null) {
            marriage = marriages.get(Marriage.getParentsGenCode(wife, husband));
        }
        return marriage;
    }

    public static Marriage findMarriage(Marriage newMarriage) {
        Marriage marriage = marriages.get(newMarriage.getKey());
        if (marriage == null) {
            marriage = marriages.get(newMarriage.getKeyReversed());
        }
        return marriage;
    }

    public static void addMarriage(Marriage newMarriage) {
        Marriage existingMarriage = findMarriage(newMarriage);
        if (existingMarriage != null) {
            //System.out.println(
            throw new RuntimeException(
                    "Warn: ln#" + newMarriage.getSourceRow().getNumber() + " Tried to add marriage that is already there. This can happen if cousins marry each other. Make sure the marriages have the same information." +
                            "\n existing:" + existingMarriage.toFormattedString() +
                            "\n   adding:" + newMarriage.toFormattedString() +
                            "\n diff:" + StringUtils.diff(existingMarriage.toString(), newMarriage.toString()) +
                            "\n existing:" + existingMarriage +
                            "\n   adding:" + newMarriage);
        }
        marriages.put(newMarriage.getKey(), newMarriage);
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
            marriage.verifyKids();

        }
        System.out.println("Marriage Count:" + marriages.size());

    }

    public static int count() {
        return marriages.size();
    }

}
