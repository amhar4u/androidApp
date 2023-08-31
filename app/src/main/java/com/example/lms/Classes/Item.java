package com.example.lms.Classes;

public class Item {
    String Type;
    String SDate;
    String EDate;
    Long days;
    String empid;
    String uid;
    String lid;

    public Item(String type, String SDate, String EDate, long days, String empid, String uid, String lid) {
        Type = type;
        this.SDate = SDate;
        this.EDate = EDate;
        this.days = days;
        this.empid = empid;
        this.uid = uid;
        this.lid = lid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSDate() {
        return SDate;
    }

    public void setSDate(String SDate) {
        this.SDate = SDate;
    }

    public String getEDate() {
        return EDate;
    }

    public void setEDate(String EDate) {
        this.EDate = EDate;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
