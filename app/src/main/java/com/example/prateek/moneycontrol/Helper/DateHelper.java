package com.example.prateek.moneycontrol.Helper;

import java.util.Calendar;

public class DateHelper {

    Calendar calendar = Calendar.getInstance();

    public String getCurrDate() {
        return String.valueOf(calendar.get(Calendar.DATE));
    }

    public String getCurrYear() {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getFullDate() {
        String month = getCurrMonth(calendar.get(Calendar.MONTH));
        return (String.valueOf(calendar.get(Calendar.DATE)) + " " + month + " " + String.valueOf(calendar.get(Calendar.YEAR)));
    }

    public String getCurrMonth(int n) {
        switch (n) {
            case 1:
                return "JAN";

            case 2:
                return "FEB";

            case 3:
                return "MAR";

            case 4:
                return "APRIL";

            case 5:
                return "MAY";

            case 6:
                return "JUNE";

            case 7:
                return "JULY";

            case 8:
                return "AUG";

            case 9:
                return "SEPT";

            case 10:
                return "OCT";

            case 11:
                return "NOV";

            case 12:
                return "DEC";

            default:
                return "???";
        }
    }

}
