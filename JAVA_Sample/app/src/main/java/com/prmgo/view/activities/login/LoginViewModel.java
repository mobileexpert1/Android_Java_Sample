package com.prmgo.view.activities.login;

import android.app.Application;

import com.prmgo.utilss.Constants;
import com.prmgo.utilss.SharedPref;
import com.prmgo.view.fragments.currentjob.currentModel.CurrentJourneyModel;
import com.prmgo.view.fragments.currentjob.webrequest.PassengerWebRequest;
import com.prmgo.view.fragments.form.webrequest.IAddPassengerHandler;
import com.prmgo.view.activities.login.model.login.Response;
import com.prmgo.view.activities.login.webrequest.ILoginHandler;
import com.prmgo.view.activities.login.webrequest.LoginWebRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private LoginWebRequest loginRequest;
    private LoginActivity context;
    private MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private MutableLiveData<Response> successRes = new MutableLiveData<>();

    private MutableLiveData<CurrentJourneyModel> checkJourney = new MutableLiveData<>();
    private MutableLiveData<String> checkJourneyError = new MutableLiveData<>();
    private SharedPref pref;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public LiveData<String> getJourneyErrorMsg() {
        return checkJourneyError;
    }

    public LiveData<Response> getSuccessRes() {
        return successRes;
    }

    public LiveData<CurrentJourneyModel> getgetJourneyStatus() {
        return checkJourney;
    }

    public void setContext(LoginActivity loginActivity) {
        this.context = loginActivity;
        pref = new SharedPref(loginActivity);

    }



    public void getToken(String userName, String password, String deviceId) {
        if (userName.isEmpty()) {
            errorMsg.setValue("Please enter Id number");
        } else if (password.isEmpty()) {
            errorMsg.setValue("Please enter Password");
        } else {
            LoginWebRequest request = new LoginWebRequest(context);
            context.showProgress();
            request.getAppToken(userName, password, new ILoginHandler.iTokenHandler() {
                @Override
                public void success(String token) {
                    context.hideProgress();
                    context.pref.setString(Constants.ACCESS_TOKEN, token);
                    loginToserver(userName, password, token,deviceId);
                }

                @Override
                public void failure(String error) {
                    context.hideProgress();
                    errorMsg.setValue(error);

                }
            });
        }
    }


    public void loginToserver(String userName, String password, String token, String deviceId) {
        LoginWebRequest request = new LoginWebRequest(context);
        context.showProgress();
        request.loginThroughServer(userName, password, token,deviceId, new ILoginHandler.iLoginResponseHandler() {
            @Override
            public void success(Response data) {

                context.hideProgress();
                successRes.setValue(data);
            }

            @Override
            public void wrongUser() {
                context.hideProgress();
                errorMsg.setValue("Device is not registered, Please try to login with authorized device.");
            }

            @Override
            public void failure(String error) {
                context.hideProgress();
                errorMsg.setValue(error);
            }
        });
    }


    public void getCurrentPassengerJourney() {
        PassengerWebRequest request = new PassengerWebRequest(context);
        request.getCurrentJourneyDetail(pref.getString(Constants.ACCESS_TOKEN), pref.getString(Constants.USER_ID), new IAddPassengerHandler.CurrentPassengerDetailHandler() {

            @Override
            public void success(CurrentJourneyModel data) {

                if (data.getIsRequestSuccessfull()) {
                    if (data.getResponse() == null) {
                        checkJourneyError.setValue("");
                    } else {
                        checkJourney.setValue(data);
                    }
                } else {
                    checkJourneyError.setValue("");
                }
            }

            @Override
            public void sessionExpired() {

            }

            @Override
            public void failure(String errorMsg) {
                checkJourneyError.setValue("");
            }
        });


    }
}
