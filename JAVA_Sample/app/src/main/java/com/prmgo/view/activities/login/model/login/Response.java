package com.prmgo.view.activities.login.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("designeeNumber")
    @Expose
    private String designeeNumber;
    @SerializedName("primaryClient")
    @Expose
    private Object primaryClient;

    @SerializedName("userToken")
    String userToken;

    @SerializedName("airportCode")
    String airportCode;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return roleName;
    }

    public void setUserRole(String userRole) {
        this.roleName = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDesigneeNumber() {
        return designeeNumber;
    }

    public void setDesigneeNumber(String designeeNumber) {
        this.designeeNumber = designeeNumber;
    }

    public Object getPrimaryClient() {
        return primaryClient;
    }

    public void setPrimaryClient(Object primaryClient) {
        this.primaryClient = primaryClient;
    }

}