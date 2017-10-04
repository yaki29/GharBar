package com.example.user.gharbar.Models;

import com.cloudant.sync.documentstore.DocumentRevision;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul on 03-10-2017.
 */

public class Place {
    String placeName;
    String addLine1;
    String addLine2;
    String city;
    String state;
    String bhk;
    String proprieterID;
    String tenantID;
    String startingPrice;

    boolean lift;
    boolean gym;
    boolean cafeteria;
    boolean swimmingpool;
    boolean garden;

    boolean validation;

    boolean apartment;
    boolean house;

    private Place() {}
    public Place(String placeName, String addLine1, String addLine2,String city,String state,String bhk,String startingPrice,String proprieterID,boolean lift,boolean garden,boolean swimmingpool,boolean gym,boolean cafeteria,boolean house,boolean apartment){
        this.placeName = placeName;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.city = city;
        this.state= state;
        this.bhk = bhk;
        this.proprieterID = proprieterID;
        this.tenantID = null;
        this.startingPrice=startingPrice;
        this.lift=lift;
        this.gym=gym;
        this.cafeteria=cafeteria;
        this.swimmingpool = swimmingpool;
        this.garden=garden;
        this.apartment=apartment;
        this.house=house;
        this.validation=false;
    }

    public boolean getValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    public boolean isLift() {
        return lift;
    }

    public void setLift(boolean lift) {
        this.lift = lift;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isCafeteria() {
        return cafeteria;
    }

    public void setCafeteria(boolean cafeteria) {
        this.cafeteria = cafeteria;
    }

    public boolean isSwimmingpool() {
        return swimmingpool;
    }

    public void setSwimmingpool(boolean swimmingpool) {
        this.swimmingpool = swimmingpool;
    }

    public boolean isGarden() {
        return garden;
    }

    public void setGarden(boolean garden) {
        this.garden = garden;
    }

    public boolean isApartment() {
        return apartment;
    }

    public void setApartment(boolean apartment) {
        this.apartment = apartment;
    }

    public boolean isHouse() {
        return house;
    }

    public void setHouse(boolean house) {
        this.house = house;
    }

    static final String DOC_TYPE = "com.cloudant.sync.example.user";
    private String type = DOC_TYPE;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


    public String getProprieterID() {
        return proprieterID;
    }

    public void setProprieterID(String proprieterID) {
        this.proprieterID = proprieterID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
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

    public String getBhk() {
        return bhk;
    }

    public void setBhk(String bhk) {
        this.bhk = bhk;
    }

    public DocumentRevision getRev() {
        return rev;
    }

    public void setRev(DocumentRevision rev) {
        this.rev = rev;
    }

    private DocumentRevision rev;



    public static Place fromRevision(DocumentRevision rev) {
        Place t = new Place();
        t.rev = rev;
        // this could also be done by a fancy object mapper
        Map<String, Object> map = rev.getBody().asMap();
        if(map.containsKey("type") && map.get("type").equals(User.DOC_TYPE)) {
            t.setType((String) map.get("type"));
            t.setPlaceName((String) map.get("placeName"));
            t.setAddLine1((String) map.get("addLine1"));
            t.setAddLine2((String) map.get("addLine2"));
            t.setCity((String) map.get("city"));
            t.setState((String)map.get("state"));
            t.setBhk((String)map.get("bhk"));
            t.setProprieterID((String)map.get("proprieterID"));
            t.setTenantID((String)map.get("tenantID"));
            t.setStartingPrice((String)map.get("startingPrice"));
            t.setCafeteria((Boolean)map.get("cafeteria"));
            t.setGarden((Boolean)map.get("garden"));
            t.setGym((Boolean)map.get("gym"));
            t.setSwimmingpool((Boolean)map.get("swimmingpool"));
            t.setLift((Boolean)map.get("lift"));
            t.setApartment((Boolean)map.get("apartment"));
            t.setHouse((Boolean)map.get("house"));
            t.setValidation((Boolean)map.get("validation"));
            return t;
        }
        return null;
    }

    public Map<String, Object> asMap() {
        // this could also be done by a fancy object mapper
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("placeName", placeName);
        map.put("addLine1", addLine1);
        map.put("addLine2", addLine2);
        map.put("city",city);
        map.put("state",state);
        map.put("bhk",bhk);
        map.put("proprieterID",proprieterID);
        map.put("tenantID",tenantID);
        map.put("startingPrice",startingPrice);
        map.put("cafeteria",cafeteria);
        map.put("garden",garden);
        map.put("gym",gym);
        map.put("swimmingpool",swimmingpool);
        map.put("lift",lift);
        map.put("apartment",apartment);
        map.put("house",house);
        map.put("validation",validation);
        return map;
    }
}
