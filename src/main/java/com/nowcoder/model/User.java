package com.nowcoder.model;


public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
    private String email;
    //0 邮件为激活;1 正常使用 ; 2 账号注销 禁止使用
    private int status;

    public User() {

    }

    public User(int id, String name, String password, String salt, String headUrl, String email, int status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.headUrl = headUrl;
        this.email = email;
        this.status = status;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String
    toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}