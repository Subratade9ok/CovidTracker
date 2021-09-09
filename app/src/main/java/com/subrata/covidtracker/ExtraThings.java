package com.subrata.covidtracker;

import android.icu.text.DecimalFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ExtraThings {


    private static final int REQUEST_CODE = 10;
    private static final String INTENT_DATA_KEY = "country_name";
    private static long timeData = 0;



    public static int getRequestCode() {
        return REQUEST_CODE;
    }

    public static String getIntentDataKey() {
        return INTENT_DATA_KEY;
    }

    public static long getTimeData() {
        return timeData;
    }

    public static void setTimeData(long timeData) {
        ExtraThings.timeData = timeData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getPercent(String data, String total){
        final String s = new DecimalFormat("#.##").format((Double.parseDouble(data) * 100) / Double.parseDouble(total)) + "%";
        return s;
    }

}
