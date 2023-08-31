package com.example.lms.Classes;

import java.sql.Time;
import java.util.Date;

public class Leave {
    private String Lid;
    private String Uid;
    private String Type;
    private String Sdate;
    private String Edate;
    private long Days;
    private String Status;
    private String Empid;
    private String CurrentTime; // Add this field for storing current time

    public Leave() {}

    public Leave(String lid, String uid, String type, String sdate, String edate, long days, String status, String empid, String currentTime) {
        Lid = lid;
        Uid = uid;
        Type = type;
        Sdate = sdate;
        Edate = edate;
        Days = days;
        Status = status;
        Empid = empid;
        CurrentTime = currentTime; // Set the current time
    }

    public String getLid() {
        return Lid;
    }

    public void setLid(String lid) {
        Lid = lid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSdate() {
        return Sdate;
    }

    public void setSdate(String sdate) {
        Sdate = sdate;
    }

    public String getEdate() {
        return Edate;
    }

    public void setEdate(String edate) {
        Edate = edate;
    }

    public long getDays() {
        return Days;
    }

    public void setDays(long days) {
        Days = days;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEmpid() {
        return Empid;
    }

    public void setEmpid(String empid) {
        Empid = empid;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }
}
