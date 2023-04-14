package com.swansong.familytree.model;

public enum Source {
    Main,
    Spouse,
    Parents,
    SpousesParents,
    Children;

    @Override
    public String toString() {
        return switch (this) {
            case Main -> "M";
            case Spouse -> "S";
            case Parents -> "P";
            case SpousesParents -> "sp";
            case Children -> "C";
        };
    }
}



