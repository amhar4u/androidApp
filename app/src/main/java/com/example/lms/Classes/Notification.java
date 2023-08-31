package com.example.lms.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {


    private String Id;
    private String Title;
    private String date;
    private String time;
    private String propic;

    private String Subject;

    private String Status;


    public Notification() {
    }

    public Notification(String title, String date, String time, String propic, String id, String subject, String status) {
        Title = title;
        this.date = date;
        this.time = time;
        this.propic = propic;
        Id = id;
        Subject = subject;
        Status = status;
    }

    // Getters and setters (optional, but helpful for future expansion)

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(this.date);
            long diff = new Date().getTime() - date.getTime();
            int days = (int) (diff / (24 * 60 * 60 * 1000));

            if (days == 0) {
                return "Today";
            } else if (days==1) {
                return String.format(Locale.getDefault(), "%ddy ago", days);
            } else {
                return String.format(Locale.getDefault(), "%ddys ago", days);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.date;
        }
    }


    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date currentTime = new Date();
            Date time = sdf.parse(this.time);
            long diff = currentTime.getTime() - time.getTime();

            if (diff < 60 * 1000) {

                int seconds = (int) (diff / 1000);
                return String.format(Locale.getDefault(), "%d sec", seconds);
            } else if (diff < 60 * 60 * 1000) {

                int minutes = (int) (diff / (60 * 1000));
                return String.format(Locale.getDefault(), "%d min", minutes);
            } else {

                int hours = (int) (diff / (60 * 60 * 1000));
                return String.format(Locale.getDefault(), "%d hr", hours);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.time;
        }
    }

}



