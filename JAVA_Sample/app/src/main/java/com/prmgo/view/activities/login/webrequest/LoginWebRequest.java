package com.prmgo.view.activities.login.webrequest;

import android.content.Context;
import com.prmgo.R;
import com.prmgo.api.APIClient;
import com.prmgo.api.APIInterface;
import com.prmgo.utilss.Constants;
import com.prmgo.view.activities.login.LoginUserRequest;
import com.prmgo.view.activities.login.model.login.LoginResponseData;
import com.prmgo.view.activities.login.model.token.AccountTokenResponse;
import com.prmgo.view.activities.login.model.token.TokenRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// this class contain the code of webservices  for get header token and login user

public class LoginWebRequest {

    private Context context;

    public LoginWebRequest(Context context) {
        this.context = context;
    }


    // get the Header token from the server for login and rest other APIS
    public void getAppToken(String userName, String password, ILoginHandler.iTokenHandler iLoginHandler) {
       // Utils.baseShowProgressDialog(context);
        Retrofit client = APIClient.getClient(context);
        APIInterface webservices = client.create(APIInterface.class);
        TokenRequest data = new TokenRequest();
        data.setPassword(Constants.TOKEN_REQUEST_PASSWORD);
        data.setUserName(Constants.TOKEN_REQUEST_USERNAME);
        Call<AccountTokenResponse> callback = webservices.getToken(data);
        callback.enqueue(new Callback<AccountTokenResponse>() {
            @Override
            public void onResponse(Call<AccountTokenResponse> call, Response<AccountTokenResponse> response) {
                //Utils.baseHideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getIsRequestSuccessfull()) {
                        iLoginHandler.success(response.body().getResponse().getToken());
                    } else {
                        iLoginHandler.failure(response.body().getMessage());
                    }
                } else {
                    iLoginHandler.failure(context.getResources().getString(R.string.went_wrong_msg));
                }
            }

            @Override
            public void onFailure(Call<AccountTokenResponse> call, Throwable t) {
                // Utils.baseHideProgressDialog();
                iLoginHandler.failure(context.getResources().getString(R.string.went_wrong_msg));
            }
        });
    }


    // Login in the application using email id and password
    public void loginThroughServer(String userName, String password, String token,String deviceId, ILoginHandler.iLoginResponseHandler iLoginHandler) {
      //  Utils.baseShowProgressDialog(context);
        Retrofit client = APIClient.getClientWithHeaderLogin(context,token,deviceId,userName);
        APIInterface webservices = client.create(APIInterface.class);
        LoginUserRequest data = new LoginUserRequest();
        data.setPassword(password);
        data.setUserName(userName);
        Call<LoginResponseData> callback = webservices.loginToServer(data);
        callback.enqueue(new Callback<LoginResponseData>() {
            @Override
            public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
               // Utils.baseHideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getIsRequestSuccessfull()) {
                        if (response.body().getResponse() != null) {
                            iLoginHandler.success(response.body().getResponse());
                        } else {
                            iLoginHandler.failure(response.body().getMessage());
                        }
                    } else {
                        iLoginHandler.failure(response.body().getMessage());
                    }
                }else if(response.code()==403)
                {
                    iLoginHandler.wrongUser();
                }else {
                    iLoginHandler.failure(context.getResources().getString(R.string.went_wrong_msg));
                }
            }

            @Override
            public void onFailure(Call<LoginResponseData> call, Throwable t) {
               // Utils.baseHideProgressDialog();
                iLoginHandler.failure(context.getResources().getString(R.string.went_wrong_msg));
            }
        });
    }
}
