package com.prmgo.view.activities.login.model.token;

public class TokenRequest {

    private String UserName;
    private String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
