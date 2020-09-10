package ir.timurid.smarttask.utils;

import java.util.Calendar;
import java.util.Date;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class DateUtils {

    public static String dateToStr(Date date) {
        if (date == null) return null;
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(date);
        return persianCalendar.getPersianShortDate();
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return persianCalendar.getTime();
    }

    public static long getRemainingDays(Date deadline) {
        if (deadline == null) return 0;

        Date today = getToday();
        long days = deadline.getTime() - today.getTime();

        if (days < 0) return 0;

        days /= 1000;
        days /= 60;
        days /= 60;
        days = days/24 + (days % 24 != 0 ? 1 : 0);
        return days;
    }
}
