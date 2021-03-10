package vn.mrlongg71.service.model.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "log")
public class Log {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "createAt")
    public String createAt;
    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lng")
    public double lng;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
