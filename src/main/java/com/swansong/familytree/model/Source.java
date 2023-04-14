package com.swansong.familytree.model;

public enum Source {
    Main,
    Spouse,
    Parents,
    SpousesParents;

    @Override
    public String toString() {
        return switch (this) {
            case Main -> "Main";
            case Spouse -> "Spouse";
            case Parents -> "Parents";
            case SpousesParents -> "SpousesParents";
        };
    }
}



