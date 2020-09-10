package ir.timurid.smarttask.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.model.TodoInfo;


@androidx.room.Database(entities = {TodoInfo.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters(TypeConverter.class)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;


    public static Database getInstance(Context ctx) {
        if (INSTANCE == null) synchronized (Database.class) {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(ctx, Database.class, "smartTaskDB")
                        .addCallback(getCallback(ctx))
                        .build();
        }

        return INSTANCE;
    }


    public abstract TodoDao todoDao();

    public abstract CategoryDao categoryDao();


    private static Callback getCallback(Context context) {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                AsyncTask.execute(() -> {
                    Database database = getInstance(context);

                    Category defaultCategory = new Category();
                    defaultCategory.setTitle(context.getResources().getString(R.string.title_defaultCategory));
                    defaultCategory.setColor("#03DAC5"); // TODO: 8/25/2020 make it dynamic, so it happen automatically
                    database.categoryDao().insert(defaultCategory);
                });
            }
        };
    }
}

