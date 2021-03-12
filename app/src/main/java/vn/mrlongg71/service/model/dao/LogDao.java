package vn.mrlongg71.service.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import vn.mrlongg71.service.model.database.LogFollow;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LogDao {
    @Insert(onConflict = REPLACE)
    void insertLog(LogFollow logFollow);

    @Query("SELECT * FROM LogFollow WHERE isSync = 0")
    List<LogFollow> getListLogFollow();

    @Query("UPDATE LogFollow SET isSync = 1")
    void updateLogFollow();

    @Query("DELETE FROM LogFollow")
    void delete();



}
