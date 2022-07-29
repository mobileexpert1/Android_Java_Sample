package com.prmgo.api;



import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.prmgo.view.activities.login.model.token.AccountTokenResponse;
import com.prmgo.view.activities.login.model.login.LoginResponseData;
import com.prmgo.view.activities.login.model.token.TokenRequest;
import com.prmgo.view.activities.login.LoginUserRequest;
import org.json.JSONArray;

import java.util.ArrayList;



public interface APIInterface {


    @POST("api/Account/token")
    Call<AccountTokenResponse> getToken(@Body TokenRequest data);

    @POST("api/Account/Login")
    Call<LoginResponseData> loginToServer(@Body LoginUserRequest data);

    
}
