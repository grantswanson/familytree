package com.swansong.familytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.swansong.familytree.StringUtilities.*;

@Data
@NoArgsConstructor
public class Name {

    private static final int MAX_DIFF_FOR_SIMILARITY = 2;
    private static final Set<String> COMMON_SUFFIXES = new HashSet<>(Set.of(
            "Sr", "Jr", "Iii"));
    private String firstNames = "";
    private String surName = "";
    private String suffix = "";

    private String nickName = "";
    private Set<String> altNames = new LinkedHashSet<>();
    private Set<String> marriedNames = new LinkedHashSet<>();

    private boolean hasSpecialNote;


    public static Name parseFullName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It is null");
        }
        Name name = new Name();
        // remove the alt names first
        name.altNames = extractAltNames(str);
        str = extractStringBeforeAlt(str);

        name.hasSpecialNote = str.contains("*");
        str = str.replace("*", "");

        str = str.replace(".", "");

        String[] names = str.trim().split(",");
        if (names.length == 0) {
            // the str="," so it is basically blank, so we shouldn't really be parsing that name.
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It is names.length:" + names.length + " str:'" + str + "'");
        } else if (names.length > 3) {
            throw new IllegalArgumentException("Unexpected lastName, firstName format: It contains an extra comma: '" + str + "'");
        }
        // if just first name and last name, and last name is a common suffix (Jr, Sr, III)
        // then the last name is really as suffix
        if (names.length == 2 &&
                COMMON_SUFFIXES.contains(toNameCase(names[1]).trim())) {
            // add an element to the front of the array to be a blank surname
            // use the new array instead
            names = Stream.concat(Stream.of(""), Stream.of(names))
                    .toArray(String[]::new);
        }

        name.setSurName(toNameCase(names[0]).trim());
        if (names.length >= 2) {
            String n = names[1];
            name.setFirstNames(toNameCase(extractFirstNames(n).trim()));
            name.setNickName(toNameCase(extractNickName(n).trim()));
            name.setMarriedNames(extractMarriedNames(n));
        }
        if (names.length == 3) {
            name.setSuffix(toNameCase(names[2]).trim());

        }
        return name;
    }


    public String toFullName() {
        StringBuilder str = new StringBuilder(surName + ", " + firstNames);
        if (nickName != null && !nickName.isEmpty()) str.append(" \"").append(nickName).append("\"");
        str.append(marriedNames.stream()
                .map(s -> " [" + s + "]")
                .collect(Collectors.joining()));
        if (suffix != null && !suffix.isEmpty()) str.append(", ").append(suffix);
        if (altNames.size() > 0) {
            str.append(" alt:");
            for (String name : altNames) {
                str.append(" ").append(name);
            }
        }
        return str.toString();
    }

    public boolean startsWith(Name n2) {
        if (!surName.equalsIgnoreCase(n2.surName) && //surnames don't match
                !surName.isBlank() && !n2.surName.isBlank()) { // and neither is blank
            return false;
        } // else surnames match, or one of them is blank (therefore allow match)

        if (!firstNames.startsWith(n2.firstNames) && // firstNames don't start with each other
                !n2.firstNames.startsWith(firstNames)) {
            return false;

        } else if (!suffix.equalsIgnoreCase(n2.suffix)) {
            if (!suffix.equalsIgnoreCase("Jr") && !n2.suffix.equalsIgnoreCase("Sr") ||
                    !n2.suffix.equalsIgnoreCase("Jr") && !suffix.equalsIgnoreCase("Sr")) {
                System.out.println("Warning: The two names match the first and last names, but NOT the suffix. Name1:"
                        + this + " Name2:" + n2);
            }
            return false;
        } else {
            return true;
        }
    }


    private static String extractNickName(String s) {
        return extractTextBetween(s, "\"", "\"");
    }

    private static Set<String> extractMarriedNames(String s) {
        Set<String> retVal = extractMarriedNames(s, "[", "]");
        retVal.addAll(extractMarriedNames(s, "(", ")"));

        return retVal;
    }

    private static Set<String> extractMarriedNames(String s, String begin, String end) {
        Set<String> retVal = new LinkedHashSet<>();
        String name;
        do {
            name = extractTextBetween(s, begin, end);
            s = removeTextBetween(s, begin, end);
            if (!name.isBlank()) {
                retVal.add(toNameCase(name).trim());
            }
        } while (!name.isBlank());
        return retVal;
    }


    private static String extractFirstNames(String s) {
        s = removeTextBetween(s, "\"", "\""); // remove nickname
        s = removeALLTextBetween(s, "[", "]"); // remove marriedNames
        s = removeALLTextBetween(s, "(", ")"); // remove marriedNames
        return s;
    }

    public static Name extractChildrensName(String name) {
        if (name != null && !name.replace("*", "").isBlank()) {
            return Name.parseFullName(
                    addCommaIfMissing(name.trim()));

        }
        return null;
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
            // if not JR/SR, then print warning
            if (!n1.suffix.equalsIgnoreCase("Jr") && !n2.suffix.equalsIgnoreCase("Sr") ||
                    !n2.suffix.equalsIgnoreCase("Jr") && !n1.suffix.equalsIgnoreCase("Sr")) {
                System.out.println("Warning: The two names match in everything but the suffix. Name1:" + n1 + " Name2:" + n2);
            }
            allowed = false;
        }
        return allowed;

    }

    public void mergeInName(Name n1) {
        mergeInFirstNames(n1);
        mergeInSurName(n1);
        mergeInNickName(n1);
        mergeInMarriedName(n1);
        mergeInSuffix(n1);
        mergeInAltNames(n1);

    }

    public void mergeStartsWith(Name altName) {
        mergeInFirstNameMiddleInitial(altName);

        mergeInSurName(altName);
        mergeInNickName(altName);
        mergeInMarriedName(altName);
        mergeInSuffix(altName);
        mergeInAltNames(altName);

    }

    public void mergeInMisspelledName(Name altName) {
        mergeInNickName(altName);
        mergeInMarriedName(altName);
        mergeInSuffix(altName);

        altName.mergeInNickName(this);
        altName.mergeInMarriedName(this);
        altName.mergeInSuffix(this);
        if (altName.surName.isBlank()) {
            altName.surName = surName;
        }
        if (altName.firstNames.isBlank()) {
            altName.firstNames = firstNames;
        }

        Set<String> differentNames = differentNames(toFullName(), altName.toFullName());
        altNames.addAll(differentNames);

    }

    private static Set<String> differentNames(String fullName, String altFullName) {
        Set<String> names1 = new HashSet<>(Arrays.asList(toNameCase(fullName).split("\\s+")));
        Set<String> names2 = new HashSet<>(Arrays.asList(toNameCase(altFullName).split("\\s+")));
        names2.removeAll(names1);
        return names2;
    }

    private void mergeInFirstNameMiddleInitial(Name n1) {
        // only call this if first names match and one has a middle initial
        // set the first names to the longest firstnames

        if (firstNames.length() < n1.firstNames.length()) {
            firstNames = n1.firstNames;
        }// else leave firstnames as is
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

    private void mergeInAltNames(Name n1) {
        altNames.addAll(n1.altNames);
    }


    public static boolean areNamesPossiblyMisspelled(Name name1, Name name2) {
        if (name1.suffix != null && !name1.suffix.isBlank() && !name1.suffix.equalsIgnoreCase(name2.suffix)) {
            return false;
        }
        // nickname, and married name don't matter
        boolean allowBlank = true; // if one of the two names is blank, then return true
        //noinspection
        boolean similar = (areNamesPossiblyMisspelled(name1.firstNames, name2.firstNames, allowBlank) &&
                areNamesPossiblyMisspelled(name1.surName, name2.surName, allowBlank));
        // if name1 has only a firstname and no surName and name2 is the reverse (only surname and not first),
        // then it would always return true and it probably shouldn't. So return false.
        if (name1.firstNames.isBlank() && name2.surName.isBlank() ||
                name2.firstNames.isBlank() && name1.surName.isBlank()) {
            similar = false;
        }


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

    public boolean isBlank() {
        return surName.isBlank() &&
                firstNames.isBlank() &&
                nickName.isBlank() &&
                marriedNames.size() == 0 &&
                suffix.isBlank();
    }

    public static boolean isOnlySurname(String name) {
        return name.trim().endsWith(",");
    }


    public String toNameKey() {
        // only firstname, surname and suffix
        // no nicknames, married names or alt names
        StringBuilder str = new StringBuilder(surName + ", " + firstNames);
        if (suffix != null && !suffix.isEmpty()) str.append(", ").append(suffix);
        return str.toString();
    }
}