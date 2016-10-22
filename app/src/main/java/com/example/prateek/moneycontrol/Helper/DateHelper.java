package com.example.prateek.moneycontrol.Helper;

import java.util.Calendar;

public class DateHelper {

    Calendar calendar = Calendar.getInstance();

    public String getCurrDate() {
        return String.valueOf(calendar.get(Calendar.DATE));
    }

    public String getFullDate() {
        String month = getCurrMonth(calendar.get(Calendar.MONTH));
        return (String.valueOf(calendar.get(Calendar.DATE)) + " " + month + " " + String.valueOf(calendar.get(Calendar.YEAR)));
    }

    public String getCurrMonth(int n) {
        switch (n) {
            case 0:
                return "JAN";

            case 1:
                return "FEB";

            case 2:
                return "MAR";

            case 3:
                return "APRIL";

            case 4:
                return "MAY";

            case 5:
                return "JUNE";

            case 6:
                return "JULY";

            case 7:
                return "AUG";

            case 8:
                return "SEPT";

            case 9:
                return "OCT";

            case 10:
                return "NOV";

            case 11:
                return "DEC";

            default:
                return "???";
        }
    }

}
