package ir.timurid.smarttask.db;

import java.util.Date;

public class TypeConverter {


    @androidx.room.TypeConverter
    public static Date toDate(Long date) {
        return date == null ? null : new Date(date);
    }

    @androidx.room.TypeConverter
    public static Long toLong(Date date) {
        return date == null ? null : date.getTime();
    }

}
