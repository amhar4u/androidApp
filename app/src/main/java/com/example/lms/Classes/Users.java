package com.example.lms.Classes;

public class Users {
    private String Uid;
    private String Name;
    private String Email;
    private String Password;
    private String Profilepic;
    private String Contact;
    private String Address;
    private String Empid; // Add the empid field

    public Users() {
    }

    public Users(String uid, String name, String email, String password, String profilepic, String contact, String address, String empid) {
        Uid = uid;
        Name = name;
        Email = email;
        Password = password;
        Profilepic = profilepic;
        Contact = contact;
        Address = address;
        Empid = empid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmpid() {
        return Empid;
    }

    public void setEmpid(String empid) {
        Empid = empid;
    }
}
