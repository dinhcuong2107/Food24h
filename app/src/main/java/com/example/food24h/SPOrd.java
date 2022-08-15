package com.example.food24h;

public class SPOrd {
    public String key_user_ord;
    public String key_sp_ord;
    public String sl_sp_ord;
    public String time_ord;
    public String day_ord;
    public String type;
    public String temp;
    public String status;
    public String add;

    public SPOrd() {
    }

    public SPOrd(String key_user_ord, String key_sp_ord, String sl_sp_ord, String time_ord, String day_ord, String type, String temp, String status, String add) {
        this.key_user_ord = key_user_ord;
        this.key_sp_ord = key_sp_ord;
        this.sl_sp_ord = sl_sp_ord;
        this.time_ord = time_ord;
        this.day_ord = day_ord;
        this.type = type;
        this.temp = temp;
        this.status = status;
        this.add = add;
    }
}