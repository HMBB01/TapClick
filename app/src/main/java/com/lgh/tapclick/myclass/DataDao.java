package com.lgh.tapclick.myclass;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lgh.tapclick.mybean.AppDescribe;
import com.lgh.tapclick.mybean.Coordinate;
import com.lgh.tapclick.mybean.MyAppConfig;
import com.lgh.tapclick.mybean.Widget;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@Dao
public interface DataDao {

    @Query("SELECT * FROM AppDescribe")
    Observable<List<AppDescribe>> getAllAppDescribes();

    @Query("SELECT * FROM AppDescribe WHERE appPackage = :appPackage")
    Observable<AppDescribe> getAppDescribeByPackage(String appPackage);

    @Query("SELECT * FROM Coordinate WHERE appPackage = :appPackage")
    Observable<List<Coordinate>> getCoordinatesByPackage(String appPackage);

    @Query("SELECT * FROM Widget WHERE appPackage = :appPackage")
    Observable<List<Widget>> getWidgetsByPackage(String appPackage);

    @Query("SELECT * FROM MyAppConfig WHERE id = 0")
    MyAppConfig getMyAppConfig();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Observable<Long> insertAppDescribe(AppDescribe... appDescribes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Observable<Long> insertAppDescribe(List<AppDescribe> appDescribes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Observable<Long> insertCoordinate(Coordinate... coordinates);

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //void insertWidget(Widget... widgets);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Observable<Long> insertWidget(Widget... widgets);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Observable<Long> insertMyAppConfig(MyAppConfig myAppConfig);

    @Delete
    void deleteCoordinate(Coordinate... coordinates);

    @Delete
    void deleteWidget(Widget... widgets);

    @Update
    void updateCoordinate(Coordinate... coordinates);

    @Update
    void updateWidget(Widget... widgets);

    @Update
    void updateAppDescribe(AppDescribe... appDescribe);

    @Update
    void updateMyAppConfig(MyAppConfig myAppConfig);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Observable<Long> insertAppDescribeForce(List<AppDescribe> appDescribes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Observable<Long> insertCoordinateForce(List<Coordinate> coordinates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Observable<Long> insertWidgetForce(List<Widget> widgets);
}
