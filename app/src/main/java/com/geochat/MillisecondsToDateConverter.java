package com.geochat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MillisecondsToDateConverter {
    public static String convertMillisecondsToDate(long milliseconds){
        DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date res = new Date(milliseconds);
        return obj.format(res);
    }
}
