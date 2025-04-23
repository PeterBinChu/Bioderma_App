package RecycleViewStuff;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String key;
    private String firstName;
    private String secondName;
    private String desc;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
    private Boolean administrator;
    private String productKeys;

    public User() {
    }

    public User(String email, String password, String nickname, String key, String firstName, String secondName, String desc, String phoneNumber, String country, String city, String street, Boolean administrator, String productKeys) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.key = key;
        this.firstName = firstName;
        this.secondName = secondName;
        this.desc = desc;
        this.phoneNumber=phoneNumber;
        this.country = country;
        this.city = city;
        this.street = street;
        this.administrator=administrator;
        this.productKeys=productKeys;
    }

    public String getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(String productKeys) {
        this.productKeys = productKeys;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
