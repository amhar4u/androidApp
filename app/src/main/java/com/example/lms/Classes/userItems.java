package com.example.lms.Classes;

public class userItems {
    private String empid;
    private String name;
    private String contact;
    private String profilepic;
    private String email;
    private String uid;
    private String password;

    public userItems() {
    }

    public userItems(String empid, String name, String contact, String profilepic, String email, String uid, String password) {
        this.empid = empid;
        this.name = name;
        this.contact = contact;
        this.profilepic = profilepic;
        this.email = email;
        this.uid = uid;
        this.password = password;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
