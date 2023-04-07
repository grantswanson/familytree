package com.swansong.familytree.csv;

import com.swansong.familytree.biz.ChildBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Row {
    private Integer number;
    private String updateDate;
    private String name;
    private String genCode;
    private String dob;
    private String pob;
    private String baptismDate;
    private String baptismPlace;
    private String confirmationDate;
    private String confirmationPlace;
    private String hsGradDate;
    private String hsGradPlace;
    private String deathDate;
    private String burialPlace;
    private String father;
    private String mother;
    private String occupation;
    private String marriageDate;
    private String marriagePlace;
    private String spouse;
    private String spouseDob;
    private String spousePob;
    private String spouseBaptismDate;
    private String spouseBaptismPlace;
    private String spouseConfirmationDate;
    private String spouseConfirmationPlace;
    private String spouseHsGradDate;
    private String spouseHsGradPlace;
    private String spouseDeathDate;
    private String spouseBurialPlace;
    private String spouseFather;
    private String spouseMother;
    private String spouseOccupation;
    private String unknown1;
    private String unknown2;
    private String child1;
    private String child2;
    private String child3;
    private String child4;
    private String child5;
    private String child6;
    private String child7;
    private String child8;
    private String child9;
    private String child10;
    private String child11;
    private String child12;
    private String childrenNotes;
    private String note1;
    private String note2;
    private String note3;
    private String note4;
    private String note5;
    private String note6;
    private String note7;

    public List<String> getChildren() {
        List<String> names = new ArrayList<>();

        for (int i = 1; i <= ChildBuilder.MAX_CHILDREN; i++) {
            String name = getChild(i);
            if (name != null && !name.isBlank()) {
                names.add(name);
            }
        }
        return names;
    }


    public String getChild(int x) {
        ChildBuilder.verifyChildNumber(x);

        switch (x) {
            case 1 -> {
                return child1;
            }
            case 2 -> {
                return child2;
            }
            case 3 -> {
                return child3;
            }
            case 4 -> {
                return child4;
            }
            case 5 -> {
                return child5;
            }
            case 6 -> {
                return child6;
            }
            case 7 -> {
                return child7;
            }
            case 8 -> {
                return child8;
            }
            case 9 -> {
                return child9;
            }
            case 10 -> {
                return child10;
            }
            case 11 -> {
                return child11;
            }
            case 12 -> {
                return child12;
            }
        }
        // will never happen because X verified above
        throw new RuntimeException("Unexpected Child #. Expected 1-12. Got:" + x);
    }

    public boolean hasChildrenNotes() {
        return childrenNotes != null && !childrenNotes.isBlank();
    }

    public String getNote(int i) {
        switch (i) {
            case 1 -> {
                return note1;
            }
            case 2 -> {
                return note2;
            }
            case 3 -> {
                return note3;
            }
            case 4 -> {
                return note4;
            }
            case 5 -> {
                return note5;
            }
            case 6 -> {
                return note6;
            }
            case 7 -> {
                return note7;
            }
            default -> throw new RuntimeException("Unexpected note #. Expected 1-7. Got:" + i);
            
        }
    }
}