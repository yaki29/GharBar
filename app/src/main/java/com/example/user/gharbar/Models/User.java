package com.example.user.gharbar.Models;

import com.cloudant.sync.documentstore.DocumentRevision;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 1/10/17.
 */

public class User {
    String name;
    String email;
    String password;
    String addressline1;
    String addressline2;
    String city;
    String state;
    String pincode;
    String contact;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;

    static final String DOC_TYPE = "com.cloudant.sync.example.user";
    private String type = DOC_TYPE;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    private User() {}


    public DocumentRevision getRev() {
        return rev;
    }

    public void setRev(DocumentRevision rev) {
        this.rev = rev;
    }

    private DocumentRevision rev;

    public User(String name, String email, String password,String category) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.category = category;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public static User fromRevision(DocumentRevision rev) {
        User t = new User();
        t.rev = rev;
        // this could also be done by a fancy object mapper
        Map<String, Object> map = rev.getBody().asMap();
        if(map.containsKey("type") && map.get("type").equals(User.DOC_TYPE)) {
            t.setType((String) map.get("type"));
            t.setName((String) map.get("name"));
            t.setEmail((String) map.get("email"));
            t.setPassword((String) map.get("password"));
            t.setCategory((String)map.get("category"));
            return t;
        }
        return null;
    }

    public Map<String, Object> asMap() {
        // this could also be done by a fancy object mapper
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("name", name);
        map.put("email", email);
        map.put("password",password);
        map.put("category",category);
        return map;
    }


}
