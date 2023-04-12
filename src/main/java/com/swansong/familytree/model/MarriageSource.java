package com.swansong.familytree.model;

public enum MarriageSource {
    MainAndSpouse,
    Parents,
    SpousesParents;

    @Override
    public String toString() {
        return switch (this) {
            case MainAndSpouse -> "MainAndSpouse";
            case Parents -> "Parents";
            case SpousesParents -> "SpousesParents";
        };
    }
}



