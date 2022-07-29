package com.prmgo.view.activities.login.webrequest;

import com.prmgo.view.activities.login.model.login.LoginResponseData;
import com.prmgo.view.activities.login.model.login.Response;

public interface ILoginHandler {

    interface iTokenHandler {
        public void success(String message);

        public void failure(String errorMsg);
    }
    interface iLoginResponseHandler {
        public void success(Response message);
public void wrongUser();
        public void failure(String errorMsg);
    }

}
