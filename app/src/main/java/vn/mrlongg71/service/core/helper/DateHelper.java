package vn.mrlongg71.service.core.helper;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static final String JsonDateStringFormat = "MM/dd/yyyy HH:mm:ss";

    public static String getString(Date date,String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfm = new SimpleDateFormat(format == null ? JsonDateStringFormat : format);
        return dfm.format(date);
    }

    public static Date getInvalidDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1999);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
