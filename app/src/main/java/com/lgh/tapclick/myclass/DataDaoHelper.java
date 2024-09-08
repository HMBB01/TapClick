package com.lgh.tapclick.myclass;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lgh.tapclick.mybean.AppDescribe;
import com.lgh.tapclick.mybean.Coordinate;
import com.lgh.tapclick.mybean.MyAppConfig;
import com.lgh.tapclick.mybean.Widget;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataDaoHelper {
    private static DataDao dataDao;
    private static MyAppConfig myAppConfig;

    public static void init(Context base) {
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
            Migration migration_6_7 = new Migration(6, 7) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'toast' TEXT");
                    database.execSQL("ALTER TABLE 'Coordinate' ADD COLUMN 'toast' TEXT");
                    database.execSQL("ALTER TABLE 'Widget' ADD COLUMN 'condition' INTEGER NOT NULL DEFAULT 0");
                }
            };
            Migration migration_7_8 = new Migration(7, 8) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'AppDescribe' DROP COLUMN 'onOff' ");
                }
            };

            dataDao = Room.databaseBuilder(base, MyDatabase.class, "applicationData.db")
                    .addMigrations(migration_1_2, migration_2_3, migration_3_4, migration_4_5, migration_5_6, migration_6_7, migration_7_8)
                    .build()
                    .dataDao();


            DataDaoHelper.AbstractSimpleObserver<Long> insertMyAppConfigObserver = new DataDaoHelper.AbstractSimpleObserver<Long>() {
                @Override
                public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {

                }
            };


            DataDaoHelper.getMyAppConfig(new DataDaoHelper.AbstractSimpleObserver<MyAppConfig>() {
                @Override
                public void onNext(@io.reactivex.rxjava3.annotations.NonNull MyAppConfig config) {
                    insertMyAppConfig(config, insertMyAppConfigObserver);
                }
            });
        }
    }

    public static void getMyAppConfig(AbstractSimpleObserver<MyAppConfig> observer) {
        Observable.create(new ObservableOnSubscribe<MyAppConfig>() {
                    @Override
                    public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<MyAppConfig> emitter) throws Throwable {
                        if (myAppConfig == null) {
                            myAppConfig = dataDao.getMyAppConfig();
                            if (myAppConfig == null) {
                                myAppConfig = new MyAppConfig();
                            }
                        }

                        emitter.onNext(myAppConfig);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void insertMyAppConfig(MyAppConfig config, AbstractSimpleObserver<Long> observer) {
        if (config == null) {
            return;
        }

        dataDao.insertMyAppConfig(config)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void insertWidget(Widget widget, AbstractSimpleObserver<Long> observer) {
        dataDao.insertWidget(widget)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void getAppDescribeByPackage(String appPackage, AbstractSimpleObserver<AppDescribe> observer) {
        dataDao.getAppDescribeByPackage(appPackage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void insertCoordinate(Coordinate coordinate,AbstractSimpleObserver<Long> observer) {
        dataDao.insertCoordinate(coordinate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static abstract class AbstractSimpleObserver<T> implements Observer<T> {
        private Disposable mDisposable;

        @Override
        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }

        public Disposable getDisposable() {
            return mDisposable;
        }

        public void dispose() {
            if (mDisposable != null && mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }
}
