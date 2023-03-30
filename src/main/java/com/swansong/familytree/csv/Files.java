package com.swansong.familytree.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Files {
    public static void main(String[] args) {
        List<String> latestVersionOfEachFile = findLatestFiles();
        for (String fileName : latestVersionOfEachFile) {
            System.out.println(fileName);
        }
    }

    private static List<String> findLatestFiles() {
        String directoryPath = "src/main/resources/working";
        return findLatestFiles(directoryPath);
    }

    public static List<String> findLatestFiles(String directoryPath) {
        Set<String> fileSet = buildLastNames(directoryPath);
        List<String> latestVersionOfEachFile = new ArrayList<>();
        for (String fileName : fileSet) {
            List<String> fileNames = findFilesByPrefix(fileName, directoryPath);
            Collections.sort(fileNames, Collections.reverseOrder());
            if (!fileNames.isEmpty()) {
                latestVersionOfEachFile.add(directoryPath + File.separator + fileNames.get(0));
            } else {
                throw new RuntimeException("Unexpectedly did not find latestVersionOf:" + fileName);
            }
        }
        Collections.sort(latestVersionOfEachFile);
        return latestVersionOfEachFile;

    }

    private static Set<String> buildLastNames(String directoryPath) {
        List<String> fileList = readFilesFromDirectory(directoryPath);
        Set<String> fileSet = buildLastNames(fileList);
        return fileSet;
    }

    private static Set<String> buildLastNames(List<String> list) {
        Set<String> set = list.stream()
                .map(str -> {
                    int idx = str.indexOf(".");
                    return (idx >= 0) ? str.substring(0, idx) : str;
                })
                .collect(Collectors.toSet());
        return set;
    }

    private static List<String> readFilesFromDirectory(String directoryPath) {
        List<String> filesList = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesList.add(file.getName());
                }
            }
        }
        return filesList;
    }

    private static List<String> findFilesByPrefix(String prefix, String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(prefix)) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

}
