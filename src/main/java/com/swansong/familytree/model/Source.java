package com.swansong.familytree.model;

public enum Source {
    Main,
    Spouse,
    Parents,
    ParentsOfChildWithMultipleMarriages,
    SpousesParents,
    Child; // child of someone

    @Override
    public String toString() {
        return switch (this) {
            case Main -> "M";
            case Spouse -> "S";
            case Parents -> "P";
            case ParentsOfChildWithMultipleMarriages -> "p";
            case SpousesParents -> "sp";
            case Child -> "C"; // child of someone
        };
    }
}



