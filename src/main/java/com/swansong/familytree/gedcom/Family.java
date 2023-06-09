package com.swansong.familytree.gedcom;

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
public class Family {
    private int id;
    private Integer husbandId;
    private Integer wifeId;
    private String marriageDate = "";
    private String marriagePlace = "";

    private String divorceDate = "";
    private String divorcePlace = "";

    private List<String> notes = new ArrayList<>();

    public void addNote(String note) {
        notes.add(note);
    }

    private List<Integer> childIds = new ArrayList<>();

    public static Family testFamily = Family.builder()
            .id(1)
            .husbandId(1)
            .wifeId(2)
            .childIds(List.of(3))
            .marriageDate("15 JAN 2020")
            .marriagePlace("New York")
            .build();
}
