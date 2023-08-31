package com.example.lms.Classes;

public class LeaveRequest {

    private String Empid;
    private String Type;
    private String Sdate;
    private String Edate;
    private long Days;
    private String Status;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    private String Uid;

    public String getLid() {
        return Lid;
    }

    public void setLid(String lid) {
        Lid = lid;
    }

    private String Lid;


    public LeaveRequest() {
    }


    public LeaveRequest(String empid, String type, String sdate, String edate, long days, String status, String lid,String uid) {
        Empid = empid;
        Type = type;
        Sdate = sdate;
        Edate = edate;
        Days = days;
        Status = status;
        Lid = lid;
        Uid=uid;
    }

    public String getEmpid() {
        return Empid;
    }

    public void setEmpid(String empid) {
        Empid = empid;
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
        return Long.parseLong(String.valueOf(Days));
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


}
