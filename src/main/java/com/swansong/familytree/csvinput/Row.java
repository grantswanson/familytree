package com.swansong.familytree.csvinput;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Row {
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
    private String wife;
    private String wifeDob;
    private String wifePob;
    private String wifeBaptismDate;
    private String wifeBaptismPlace;
    private String wifeConfirmationDate;
    private String wifeConfirmationPlace;
    private String wifeHsGradDate;
    private String wifeHsGradPlace;
    private String wifeDeathDate;
    private String wifeBurialPlace;
    private String wifeFather;
    private String wifeMother;
    private String wifeOccupation;
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
    private String notes1;
    private String notes2;
    private String notes3;
    private String notes4;
    private String notes5;
    private String notes6;
    private String notes7;

    public String getChild(int x) {

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
        throw new RuntimeException("Unexpected Child #. Expected 1-12. Got:"+x);
    }
}

//public class CSVRow {
//    private EnumMap<CSVColumns, String> rowData = new EnumMap<>(CSVColumns.class);
//
//        rowData.put(CSVColumns.UPDATE_DATE, "Work");
//        schedule.put(Days.TUESDAY, "Work");
//        schedule.put(Days.WEDNESDAY, "Study");
//        schedule.put(Days.THURSDAY, "Study");
//        schedule.put(Days.FRIDAY, "Relax");
//}
//public enum CSVColumns {
//
//    UPDATE_DATE,NAME,GEN_CODE,DOB,POB,BAPTISM_DATE,BAPTISM_PLACE,CONFIRMATION_DATE,CONFIRMATION_PLACE,
//    HS_GRAD_DATE,HS_GRAD_PLACE,DEATH_DATE,BURIAL_PLACE,FATHER,MOTHER,OCCUPATION,
//    MARRAIGE_DATE,MARRAIGE_PLACE,WIFE,WIFE_DOB,WIFE_POB,WIFE_BAPTISM_DATE,WIFE_BAPTISM_PLACE,
//    WIFE_CONFIRMATION_DATE,WIFE_CONFIRMATION_PLACE,WIFE_HS_GRAD_DATE,WIFE_HS_GRAD_PLACE,
//    WIFE_DEATH_DATE,WIFE_BURRIAL_PLACE,WIFE_FATHER,WIFE_MOTHER,WIFE_OCCUPATION,UNKNOWN_1,UNKNOWN_2,
//    CHILD_1,CHILD_2,CHILD_3,CHILD_4,CHILD_5,CHILD_6,CHILD_7,CHILD_8,CHILD_9,CHILD_10,CHILD_11,CHILD_12,
//    CHILDREN_NOTES,NOTES_1,NOTES_2,NOTES_3,NOTES_4,NOTES_5,NOTES_6,NOTES_7
//}