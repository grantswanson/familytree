package com.swansong.familytree.model;

import lombok.Data;

@Data
public class NameKey {
    public NameKey(String str) {
        nameKey = str;
    }

    private String nameKey = "";

    @Override
    public String toString() {
        return nameKey;
    }
}
