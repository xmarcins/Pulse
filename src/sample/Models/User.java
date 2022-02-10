package sample.Models;

import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import java.util.List;

import static sample.Extensions.ArrayExtensions.ConvertStringToIntegerList;

public class User {

    private int userId;
    private SimpleStringProperty username;
    private SimpleStringProperty mail;
    private SimpleStringProperty password;
    private SimpleStringProperty registrationDate;
    private boolean isAdmin;
    private boolean banned;
    private List<Integer> likedSongs = new ArrayList<>();

    public User(int userId, String username, String mail, String password, String registrationDate, boolean isAdmin, boolean banned, String likedSongs) {
        this.userId = userId;
        this.username = new SimpleStringProperty(username);
        this.mail = new SimpleStringProperty(mail);
        this.password = new SimpleStringProperty(password);
        this.registrationDate = new SimpleStringProperty(registrationDate);
        this.isAdmin = isAdmin;
        this.banned = banned;
        this.likedSongs = ConvertStringToIntegerList(likedSongs);
    }
    public User(int userId,String username, String mail,String registrationDate,boolean isAdmin,boolean banned){
        this.userId=userId;
        this.username=new SimpleStringProperty(username);
        this.mail=new SimpleStringProperty(mail);
        this.registrationDate=new SimpleStringProperty(registrationDate);
        this.isAdmin=isAdmin;
        this.banned=banned;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getMail() {
        return mail.get();
    }

    public SimpleStringProperty mailProperty() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail.set(mail);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRegistrationDate() {
        return registrationDate.get();
    }

    public SimpleStringProperty registrationDateProperty() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<Integer> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(List<Integer> likedSongs) {
        this.likedSongs = likedSongs;
    }
}

