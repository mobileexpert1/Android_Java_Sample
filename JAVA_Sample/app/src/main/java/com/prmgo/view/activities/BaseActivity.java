package com.prmgo.view.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.prmgo.myapp.MyApp;
import com.prmgo.R;


import com.prmgo.utilss.CircularProgressDialog;
import com.prmgo.utilss.Constants;
import com.prmgo.utilss.SharedPref;
import com.prmgo.view.activities.home.AgentDrawerActivity;
import com.prmgo.view.activities.home.AgentViewModel;
import com.prmgo.view.activities.login.LoginActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class BaseActivity extends AppCompatActivity {


    public SharedPref pref;
    public Gson gson;
    private AlertDialog dialogProgress;
    private CircularProgressDialog progressDialog;
    public AppUpdateManager appUpdateManager;
    public MyApp application;
    public static BaseActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPref(this);
        application = (MyApp) getApplicationContext();
        context = this;
        appUpdateManager = AppUpdateManagerFactory.create(this);
        gson = new Gson();
        progressDialog = new CircularProgressDialog(this);
        parseCodeDetail();
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void parseCodeDetail() {




        String[] seatTypecodes = getResources().getStringArray(R.array.classes);
        String[] seatTypeNames = getResources().getStringArray(R.array.categories);

        for (int j = 0; j < seatTypecodes.length; j++) {
            Constants.seatTypeNames.put(seatTypecodes[j], seatTypeNames[j]);
        }
    }

    public void logoutUser() {
        pref.clearData();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            133133);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }
}
