package vn.mrlongg71.service.core;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import vn.mrlongg71.service.model.room.LogFollow;
import vn.mrlongg71.service.model.room.dao.LogDao;

@Database(version = 3, entities = {LogFollow.class})
public abstract class AppDatabase extends RoomDatabase {
    @SuppressLint("StaticFieldLeak")
    private static AppDatabase appDatabase = null;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static final String DB_NAME = "Service.db3";

    public abstract LogDao getLogDao();

    public static AppDatabase getInstance(Context context) {
        AppDatabase.context = context;
        if (appDatabase == null) {
            init();
        }
        return appDatabase;
    }

    private static void init() {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(Migration_1_to_2)
                .build();
    }

    private static final Migration Migration_1_to_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
