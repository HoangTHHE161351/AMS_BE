package vn.attendance.config.authen.entity;

import lombok.Data;

@Data
public class UserDB {
    private int id;
    private String username;
    private String name;
    private String address;
    private String phone;

    public UserDB() {
    }

    public UserDB(int id, String username, String name, String address, String phone) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}
