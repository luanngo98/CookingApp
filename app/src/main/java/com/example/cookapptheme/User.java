package com.example.cookapptheme;

public class User {

    private int Id;
    private String Name;
    private String Email;
    private String Password;
    private byte[] Image;

    public User(int id, String name, String email, String password, byte[] image) {
        Id = id;
        Name = name;
        Email = email;
        Password = password;
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
