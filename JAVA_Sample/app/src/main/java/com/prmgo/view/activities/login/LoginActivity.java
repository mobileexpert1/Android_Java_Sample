package com.prmgo.view.activities.login;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prmgo.myapp.MyAppWebRequest;
import com.prmgo.view.activities.BaseActivity;
import com.prmgo.R;
import com.prmgo.utilss.Constants;
import com.prmgo.view.activities.ManagerDrawerActivity;
import com.prmgo.view.activities.home.AgentDrawerActivity;
import com.prmgo.view.fragments.currentjob.currentModel.CurrentJourneyModel;
import com.prmgo.view.activities.login.model.login.Response;


import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.userNameET)
    EditText userNameET;
    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.keyTV)
    TextView keyTV;
    @BindView(R.id.startBT)
    Button startBT;
    private LoginViewModel loginViewModel;
    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        MyAppWebRequest.lastScannedQRcode="";
        ButterKnife.bind(this);     // ButterKnife Binding
        loginViewModel();
        Log.d("###ID", getDeviceId());//  set up view model with observer
        startBT.setOnClickListener(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        keyTV.setText(getDeviceId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            keyTV.setVisibility(View.VISIBLE);
        }else
        {
            keyTV.setVisibility(View.GONE);
        }

    }

    private String getDeviceId() {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public void loginViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(this);
        loginViewModel.getSuccessRes().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(Response s) {


                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                String data = gson.toJson(s);
                pref.setString(Constants.USER_ID, s.getUserId());
                pref.setString(Constants.USER_ROLE_TYPE, s.getUserRole());
                pref.setString(Constants.DESIGNEE_NUMBER, s.getDesigneeNumber());
                pref.setString(Constants.LOGGEDIN_USER_DATA, data);
                pref.setString(Constants.SESSION_TOKEN, s.getUserToken());
                pref.setString(Constants.AGENT_AIRPORT_CODE, s.getAirportCode());

                    loginViewModel.getCurrentPassengerJourney();

            }
        });
        loginViewModel.getErrorMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getgetJourneyStatus().observe(LoginActivity.this, new Observer<CurrentJourneyModel>() {
            @Override
            public void onChanged(CurrentJourneyModel s) {
              //  application.enableBEaconLib();
                Gson gson = new Gson();
                String dataString = gson.toJson(s);
                pref.setString(Constants.PASSENGER_DATA, dataString);


                Intent in = new Intent(LoginActivity.this, AgentDrawerActivity.class);
                in.putExtra(Constants.CURRENT_JOURNEY_STATUS, true);
                startActivity(in);
                finish();


            }
        });
        loginViewModel.getJourneyErrorMsg().observe(LoginActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Intent in = new Intent(LoginActivity.this, AgentDrawerActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBT:
                //loginViewModel.getToken(userNameET.getText().toString().trim(), passwordET.getText().toString().trim());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  
                        loginViewModel.getToken(userNameET.getText().toString().trim(), passwordET.getText().toString().trim(), getDeviceId());

                } else {
                    /*Comment location permission*/

                    if (checkPermission(Manifest.permission.READ_PHONE_STATE) /*&& checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)*/) {
                        Log.d("###ID", getIMEI(this));

                        loginViewModel.getToken(userNameET.getText().toString().trim(), passwordET.getText().toString().trim(), getIMEI(this));

                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE/*, Manifest.permission.ACCESS_FINE_LOCATION*/}, 111);

                    }
                }
                break;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(LoginActivity.this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        loginViewModel.getToken(userNameET.getText().toString().trim(), passwordET.getText().toString().trim(), getDeviceId());

                    } else
                        loginViewModel.getToken(userNameET.getText().toString().trim(), passwordET.getText().toString().trim(), getIMEI(this));

                } else {
                    Toast.makeText(this, "You have denied the permission. Please accept for login.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    private String getIMEI(LoginActivity loginActivity) {
        TelephonyManager telephonyManager = (TelephonyManager) loginActivity.getSystemService(Context.TELEPHONY_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
            imei = telephonyManager.getImei();
        } else {
            imei = telephonyManager.getDeviceId();
        }

        if (imei != null && !imei.isEmpty()) {
            return imei;
        } else {
            return android.os.Build.SERIAL;
        }
    }


}
