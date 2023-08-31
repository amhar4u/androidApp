package com.example.lms.Classes;

public class ReportClass {
    private String empId;
    private String profilePicUrl;
    private int totalLeaveDays;
    private int approvedLeaveDays;
    private int rejectedLeaveDays;
    private int pendingLeaveDays;
    private int casualLeaveCount;
    private int shortLeaveCount;
    private int sickLeaveCount;
    private int balanceLeave;
    private int noPayDays;

    public ReportClass() {
    }

    public ReportClass(String empId, String profilePicUrl, int totalLeaveDays, int approvedLeaveDays, int rejectedLeaveDays, int pendingLeaveDays, int casualLeaveCount, int shortLeaveCount, int sickLeaveCount, int balanceLeave, int noPayDays) {
        this.empId = empId;
        this.profilePicUrl = profilePicUrl;
        this.totalLeaveDays = totalLeaveDays;
        this.approvedLeaveDays = approvedLeaveDays;
        this.rejectedLeaveDays = rejectedLeaveDays;
        this.pendingLeaveDays = pendingLeaveDays;
        this.casualLeaveCount = casualLeaveCount;
        this.shortLeaveCount = shortLeaveCount;
        this.sickLeaveCount = sickLeaveCount;
        this.balanceLeave = balanceLeave;
        this.noPayDays = noPayDays;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(int totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public int getApprovedLeaveDays() {
        return approvedLeaveDays;
    }

    public void setApprovedLeaveDays(int approvedLeaveDays) {
        this.approvedLeaveDays = approvedLeaveDays;
    }

    public int getRejectedLeaveDays() {
        return rejectedLeaveDays;
    }

    public void setRejectedLeaveDays(int rejectedLeaveDays) {
        this.rejectedLeaveDays = rejectedLeaveDays;
    }

    public int getPendingLeaveDays() {
        return pendingLeaveDays;
    }

    public void setPendingLeaveDays(int pendingLeaveDays) {
        this.pendingLeaveDays = pendingLeaveDays;
    }

    public int getCasualLeaveCount() {
        return casualLeaveCount;
    }

    public void setCasualLeaveCount(int casualLeaveCount) {
        this.casualLeaveCount = casualLeaveCount;
    }

    public int getShortLeaveCount() {
        return shortLeaveCount;
    }

    public void setShortLeaveCount(int shortLeaveCount) {
        this.shortLeaveCount = shortLeaveCount;
    }

    public int getSickLeaveCount() {
        return sickLeaveCount;
    }

    public void setSickLeaveCount(int sickLeaveCount) {
        this.sickLeaveCount = sickLeaveCount;
    }

    public int getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(int balanceLeave) {
        this.balanceLeave = balanceLeave;
    }

    public int getNoPayDays() {
        return noPayDays;
    }

    public void setNoPayDays(int noPayDays) {
        this.noPayDays = noPayDays;
    }
}
