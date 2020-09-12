package ir.timurid.smarttask.utils;

import java.util.Calendar;
import java.util.Date;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.timurid.smarttask.adapter.TodoAdapter;

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
        persianCalendar.set(Calendar.HOUR, 0);
        persianCalendar.set(Calendar.MINUTE, 0);
        persianCalendar.set(Calendar.SECOND, 0);
        persianCalendar.set(Calendar.MILLISECOND, 0);

        return persianCalendar.getTime();
    }

    public static int getRemainingDays(Date deadline) {
        if (deadline == null) return TodoAdapter.DEADLINE_NOT_SET;

        Date today = getToday();
        long days = deadline.getTime() - today.getTime();

        if (days == 0) return TodoAdapter.DEADLINE_TODAY;
        if (days < 0) return TodoAdapter.DEADLINE_PASSED;

        days /= 1000;
        days /= 60;
        days /= 60;
        days = days/24;
        return (int) days;
    }
}
