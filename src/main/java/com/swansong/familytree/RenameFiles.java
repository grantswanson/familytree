package com.swansong.familytree;

import java.io.File;

public class RenameFiles {

    public static void main(String[] args) {
        String directoryPath = "C:\\Users\\grant\\Desktop\\projects\\java\\familytree\\src\\main\\resources\\working"; // Change this to the path of your directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String oldName = file.getName();
                String newName = oldName.replace(".v0.csv", ".csv");

                if (!oldName.equals(newName)) {
                    File newFile = new File(directoryPath + "/" + newName);
                    boolean success = file.renameTo(newFile);
                    if (success) {
                        System.out.println("Renamed " + oldName + " to " + newName);
                    } else {
                        System.out.println("Failed to rename " + oldName);
                    }
                } else {
                    System.out.println("Skipped:" + oldName);
                }
            }
        }
    }
}
