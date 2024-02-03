package com.lgh.tapclick.myclass;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lgh.tapclick.mybean.MyAppConfig;
import com.lgh.tapclick.myfunction.MyUtils;

public class MyApplication extends Application {

    public static DataDao dataDao;
    public static MyAppConfig myAppConfig;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if (dataDao == null) {
            Migration migration_1_2 = new Migration(1, 2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'lastClickTime' INTEGER NOT NULL DEFAULT 0");
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'clickCount' INTEGER NOT NULL DEFAULT 0");
                    database.execSQL("ALTER TABLE 'Coordinate' ADD COLUMN 'lastClickTime' INTEGER NOT NULL DEFAULT 0");
                    database.execSQL("ALTER TABLE 'Coordinate' ADD COLUMN 'clickCount' INTEGER NOT NULL DEFAULT 0");
                }
            };
            Migration migration_2_3 = new Migration(2, 3) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'clickNumber' INTEGER NOT NULL DEFAULT 1");
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'clickInterval' INTEGER NOT NULL DEFAULT 500");
                }
            };
            Migration migration_3_4 = new Migration(3, 4) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'action' INTEGER NOT NULL DEFAULT 0");
                }
            };
            Migration migration_4_5 = new Migration(4, 5) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Widget' RENAME COLUMN 'lastClickTime' TO 'lastTriggerTime'");
                    database.execSQL("ALTER TABLE 'Widget' RENAME COLUMN 'clickCount' TO 'triggerCount'");
                }
            };
            Migration migration_5_6 = new Migration(5, 6) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Coordinate' RENAME COLUMN 'lastClickTime' TO 'lastTriggerTime'");
                    database.execSQL("ALTER TABLE 'Coordinate' RENAME COLUMN 'clickCount' TO 'triggerCount'");
                }
            };
            dataDao = Room.databaseBuilder(base, MyDatabase.class, "applicationData.db").addMigrations(migration_1_2, migration_2_3, migration_3_4, migration_4_5, migration_5_6).allowMainThreadQueries().build().dataDao();
        }

        if (myAppConfig == null) {
            myAppConfig = dataDao.getMyAppConfig();
            if (myAppConfig == null) {
                myAppConfig = new MyAppConfig();
                dataDao.insertMyAppConfig(myAppConfig);
            }
        }

        MyUtils.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyUncaughtExceptionHandler.getInstance(this).run();
    }
}