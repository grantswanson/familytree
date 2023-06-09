package com.swansong.familytree.utils;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static LocalDate parseDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] dateParts = date.split(" ");
        try {
            if (dateParts.length == 3) {
                String year = dateParts[0];
                String month = dateParts[1];
                String day = dateParts[2];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM d");
                return LocalDate.parse(year + " " + month + " " + day, formatter);
            } else if (dateParts.length == 2) {
                String year = dateParts[0];
                String month = dateParts[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM");
                return YearMonth.parse(year + " " + month, formatter).atEndOfMonth();
            } else if (dateParts.length == 1) {
                String year = dateParts[0];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
                return Year.parse(year, formatter).atDay(1);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean isAfter(String date1, String date2) {
        LocalDate localDate1 = parseDate(date1);
        LocalDate localDate2 = parseDate(date2);

        if (localDate1 == null || localDate2 == null) {
            return false;
        }

        int year1 = localDate1.getYear();
        int year2 = localDate2.getYear();

        if (year1 > year2) {
            return true;
        } else if (year1 < year2) {
            return false;
        }

        String[] dateParts1 = date1.split(" ");
        String[] dateParts2 = date2.split(" ");

        if (dateParts1.length == 1 || dateParts2.length == 1) {
            return false;
        }

        int month1 = localDate1.getMonthValue();
        int month2 = localDate2.getMonthValue();

        if (month1 > month2) {
            return true;
        } else if (month1 < month2) {
            return false;
        }

        if (dateParts1.length == 2 || dateParts2.length == 2) {
            return false;
        }

        int day1 = localDate1.getDayOfMonth();
        int day2 = localDate2.getDayOfMonth();

        return day1 > day2;
    }

    public static boolean isBefore(String date1, String date2) {
        LocalDate localDate1 = parseDate(date1);
        LocalDate localDate2 = parseDate(date2);

        if (localDate1 == null || localDate2 == null) {
            return false;
        }

        int year1 = localDate1.getYear();
        int year2 = localDate2.getYear();

        if (year1 < year2) {
            return true;
        } else if (year1 > year2) {
            return false;
        }

        String[] dateParts1 = date1.split(" ");
        String[] dateParts2 = date2.split(" ");

        if (dateParts1.length == 1 || dateParts2.length == 1) {
            return false;
        }

        int month1 = localDate1.getMonthValue();
        int month2 = localDate2.getMonthValue();

        if (month1 < month2) {
            return true;
        } else if (month1 > month2) {
            return false;
        }

        if (dateParts1.length == 2 || dateParts2.length == 2) {
            return false;
        }

        int day1 = localDate1.getDayOfMonth();
        int day2 = localDate2.getDayOfMonth();

        return day1 < day2;
    }

    public static boolean isValidYear(String year) {
        try {
            int y = Integer.parseInt(year);
            return y > 0 && y <= LocalDate.now().getYear();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String year, String month, String day) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM d");
            LocalDate.parse(year + " " + month + " " + day, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String convertDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        String[] dateParts = date.split(" ");
        if (dateParts.length == 3) {
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            if (isValidDate(year, month, day)) {
                return day + " " + month + " " + year;
            } else {
                return "";
            }
        } else if (dateParts.length == 2) {
            String year = dateParts[0];
            String month = dateParts[1];
            if (isValidYear(year)) {
                return month + " " + year;
            } else {
                return "";
            }
        } else if (dateParts.length == 1) {
            String year = dateParts[0];
            if (isValidYear(year)) {
                return year;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
