package com.example.food24h;

public class User {
    public String avt;
    public String fullname;
    public String dateofbirth;
    public String sex;
    public String phonenumber;
    public String password;
    public String address;
    public String position;

    public User() {
    }

    public User(String avt, String fullname, String dateofbirth, String sex, String phonenumber, String password, String address, String position) {
        this.avt = avt;
        this.fullname = fullname;
        this.dateofbirth = dateofbirth;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.password = password;
        this.address = address;
        this.position = position;
    }
}
