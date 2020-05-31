package com.barlis.dailytopnews.Common;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common
{
    public static final String APP_ID = "c443a666b8b8d6df03fa16587b2be790";

    public static Location current_location = null;

    public static String convertUnixToDate(long dt)
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd EEE MM yy");
        Date date = new Date();
        date.setTime((long)dt*1000);
        return dateFormat.format(date);
    }
}
