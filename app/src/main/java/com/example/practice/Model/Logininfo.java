package com.example.practice.Model;

public class Logininfo {

    private String Username;
    private String Password;
    private String Salt;
    private String Link;

    public Logininfo(){
    }

    public Logininfo(String user, String pass, String salt, String link){
        Username = user;
        Password = pass;
        Salt = salt;
        Link = link;
    }


    public String getSalt() {
        return Salt;
    }

    public void setSalt(String salt) {
        Salt = salt;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
