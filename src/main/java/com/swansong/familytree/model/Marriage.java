package com.swansong.familytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Marriage {

    private final static String defaultSource = "Donovan Burdette Meyer";

    public Marriage() {
        id = Id.MARRIAGE.nextId();
    }

    private String id;
    private Integer sourceLineNumber;

    private Person husband;
    private Person wife;

}
