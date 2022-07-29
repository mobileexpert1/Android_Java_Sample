package com.prmgo.view.activities.login;

public class LoginUserRequest {



        private String UserNumber;
        private String Password;

        public String getUserName() {
            return UserNumber;
        }

        public void setUserName(String userName) {
            UserNumber = userName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }


}

