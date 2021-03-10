package vn.mrlongg71.service.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import vn.mrlongg71.service.model.room.Log;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LogDao {
    @Insert(onConflict = REPLACE)
    void insertLog(Log log);

    @Query("SELECT * FROM log")
    List<Log> getListLog();
}
