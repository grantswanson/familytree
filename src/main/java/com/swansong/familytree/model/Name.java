package com.swansong.familytree.model;

import com.swansong.familytree.StringUtilities;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Name {

    private static final int MAX_DIFF_FOR_SIMILARITY = 2;
    private String firstNames = "";
    private String nickName = "";
    private String surName = "";
    private String suffix = "";
    private List<String> marriedNames = new ArrayList<>();

//    private String prefix;
//    private String title;


    public static Name parseLastCommaFirstName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It is null");
        }
        String[] names = str.split(",");
        if (names.length == 0) {
            // the str="," so it is basically blank, so we shouldn't really be parsing that name.
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It is names.length:" + names.length + " str:'" + str + "'");
        } else if (names.length > 3) {
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It contains an extra comma: '" + str + "'");
        }
        Name name = new Name();
        name.setSurName(toNameCase(removeAsterisk(names[0]).trim()));
        if (names.length >= 2) {
            String n = names[1];
            name.setFirstNames(toNameCase(extractFirstNames(removeAsterisk(n)).trim()));
            name.setNickName(toNameCase(extractNickName(removeAsterisk(n)).trim()));
            name.setMarriedNames(extractMarriedNames(n));
        }
        if (names.length == 3) {
            name.setSuffix(toNameCase(removeAsterisk(names[2])).replace(".", "").trim());

        }
        return name;
    }

    private static String extractNickName(String s) {
        return extractTextBetween(s, "\"", "\"");
    }

    private static List<String> extractMarriedNames(String s) {
        List<String> retVal = extractMarriedNames(s, "[", "]");
        retVal.addAll(extractMarriedNames(s, "(", ")"));

        return retVal;
    }

    private static List<String> extractMarriedNames(String s, String begin, String end) {
        List<String> retVal = new ArrayList<>();
        String name;
        do {
            name = removeAsterisk(extractTextBetween(s, begin, end));
            s = removeTextBetween(s, begin, end);
            if (!name.isBlank()) {
                retVal.add(toNameCase(name).trim());
            }
        } while (!name.isBlank());
        return retVal;
    }

    private static String removeALLTextBetween(String s, String begin, String end) {
        String n = extractTextBetween(s, begin, end);
        while (!n.isBlank()) {
            s = removeTextBetween(s, begin, end);
            n = extractTextBetween(s, begin, end);
        }
        return s;
    }

    private static String extractTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(firstQuoteIndex + 1, secondQuoteIndex);
        }
        return "";
    }

    private static String removeTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(0, firstQuoteIndex) + s.substring(secondQuoteIndex + 1).trim();
        }
        return s;
    }

    private static String extractFirstNames(String s) {
        s = removeTextBetween(s, "\"", "\""); // remove nickname
        s = removeALLTextBetween(s, "[", "]"); // remove marriedNames
        s = removeALLTextBetween(s, "(", ")"); // remove marriedNames
        return s;
    }

    private static String toNameCase(String s) {
        // only change the case if already in all caps
        if (StringUtilities.isAllCaps(s)) {
            return StringUtilities.toCapitalizedCase(s);
        }
        // else leave in existing case. E.g. McGee should NOT be Mcgee
        return s;
    }

    public static String addCommaIfMissing(String name) {
        if (name == null) {
            return ",";
        } else if (name.contains(",")) {
            return name;
        } else {
            return "," + name;
        }
    }

    public static String removeAsterisk(String name) {
        if (name == null) {
            return "";
        }
        return name.replace("*", "").trim();
    }

    public String getLastCommaFirst() {
        String key = surName + ", " + firstNames;
        if (nickName != null && !nickName.isEmpty()) key += " \"" + nickName + "\"";
        key += marriedNames.stream()
                .map(s -> " [" + s + "]")
                .collect(Collectors.joining());
        if (suffix != null && !suffix.isEmpty()) key += ", " + suffix;
        return key;
    }

    public static boolean isMergeAllowed(Name n1, Name n2) {
        // nickname, and married name don't matter
        boolean allowed = (n1.firstNames.equalsIgnoreCase(n2.firstNames) &&
                n1.surName.equalsIgnoreCase(n2.surName)) ||
                (n1.firstNames.equalsIgnoreCase(n2.firstNames) &&
                        (n1.surName.isBlank() || n2.surName.isBlank())) ||
                ((n1.firstNames.isBlank() || n2.firstNames.isBlank()) &&
                        n1.surName.equalsIgnoreCase(n2.surName));
        // don't allow if suffix is not the same
        if (allowed && !n1.suffix.equalsIgnoreCase(n2.suffix)) {
            System.out.println("Warning: The two names match in everything but the suffix. Name1:" + n1 + " Name2:" + n2);
            allowed = false;
        }
        return allowed;

    }

    public void mergeInName(Name n1) {
        // could have made these lamba functions, but I did not like it as much
        // mergeInField(n1, name -> name.nickName, (name, value) -> name.nickName = value);
        // mergeInField(n1, name -> name.marriedName, (name, value) -> name.marriedName = value);
        mergeInFirstNames(n1);
        mergeInSurName(n1);
        mergeInNickName(n1);
        mergeInMarriedName(n1);
        mergeInSuffix(n1);

    }

    private void mergeInFirstNames(Name n1) {
        if (n1.firstNames != null && !n1.firstNames.isBlank()) {
            if (firstNames != null && !firstNames.isBlank() && !firstNames.equalsIgnoreCase(n1.firstNames)) {
                throw new RuntimeException("Both names have non-blank and non-equal firstNames. this:" + this + " n1:" + n1);
            }
            // else
            firstNames = n1.firstNames;
        } // else n1=blank, so no merge
    }

    private void mergeInSurName(Name n1) {
        if (n1.surName != null && !n1.surName.isBlank()) {
            if (surName != null && !surName.isBlank() && !surName.equalsIgnoreCase(n1.surName)) {
                throw new RuntimeException("Both names have non-blank and non-equal surNames. this:" + this + " n1:" + n1);
            }
            // else
            surName = n1.surName;
        } // else n1=blank, so no merge
    }

    private void mergeInNickName(Name n1) {
        if (n1.nickName != null && !n1.nickName.isBlank()) {
            if (nickName != null && !nickName.isBlank() && !nickName.equalsIgnoreCase(n1.nickName)) {
                throw new RuntimeException("Both names have non-blank and non-equal nicknames. this:" + this + " n1:" + n1);
            }
            // else
            nickName = n1.nickName;
        } // else n1=blank, so no merge
    }

    private void mergeInMarriedName(Name n1) {
        marriedNames.addAll(n1.marriedNames);
    }

    private void mergeInSuffix(Name n1) {
        // should do lambdas instead
        if (n1.suffix != null && !n1.suffix.isBlank()) {
            if (suffix != null && !suffix.isBlank() && !suffix.equalsIgnoreCase(n1.suffix)) {
                throw new RuntimeException("Both names have non-blank, non-equal suffixes. this:" + this + " n1:" + n1);
            }
            // else
            suffix = n1.suffix;
        } // else n1=blank, so no merge
    }

    //    private void mergeInField(Name n1, Function<Name, String> getter, BiConsumer<Name, String> setter) {
//        String value1 = getter.apply(this);
//        String value2 = getter.apply(n1);
//        if (value2 != null && !value2.isBlank()) {
//            if (value1 != null && !value1.isBlank() && !value1.equalsIgnoreCase(value2)) {
//                throw new RuntimeException("Both names have non-blank and non-equal values. this:" + this + " n1:" + n1);
//            }
//            // else
//            setter.accept(this, value2);
//        } // else n1=blank, so no merge
//    }
    public boolean isBlank() {
        return surName.isBlank() &&
                firstNames.isBlank() &&
                nickName.isBlank() &&
                marriedNames.size() == 0 &&
                suffix.isBlank();
    }

    public static Name extractChildrensName(String name) {
        name = Name.removeAsterisk(name); // null safe, returns "" if null passed in
        if (!name.isBlank()) {
            return Name.parseLastCommaFirstName(
                    Name.addCommaIfMissing(name.trim()));
        }
        return null;
    }

    public static boolean areNamesPossiblyMisspelled(Name name1, Name name2) {
        // nickname, and married name don't matter
        boolean allowBlank = true; // if one of the two names is blank, then return true
        //noinspection UnnecessaryLocalVariable
        boolean similar = (areNamesPossiblyMisspelled(name1.firstNames, name2.firstNames, allowBlank) &&
                areNamesPossiblyMisspelled(name1.surName, name2.surName, allowBlank));

        return similar;
    }

    @SuppressWarnings("unused")
    public static boolean areNamesPossiblyMisspelled(String name1, String name2) {
        return areNamesPossiblyMisspelled(name1, name2, false);
    }

    /**
     * @param allowBlank if one of the two names is blank, then return true
     */
    public static boolean areNamesPossiblyMisspelled(String name1, String name2, boolean allowBlank) {
        if (allowBlank && (name1.isBlank() || name2.isBlank())) {
            return true;
        }
        // calculate the Levenshtein distance between the two names
        LevenshteinDistance levenshteinDistance =
                new LevenshteinDistance();
        int distance = levenshteinDistance.apply(name1.toLowerCase(), name2.toLowerCase());
        // if the distance is less than or equal to a certain threshold, return true
        return distance <= MAX_DIFF_FOR_SIMILARITY;
    }
    public static boolean isOnlySurname(String name) {
        return name.trim().endsWith(",");
    }
}