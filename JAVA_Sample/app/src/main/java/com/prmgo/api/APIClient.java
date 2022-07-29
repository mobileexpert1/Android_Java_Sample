package com.prmgo.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prmgo.utilss.Constants;
import com.prmgo.utilss.SharedPref;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    public static String BASE_URL = "BASE_URL";
   

    public static Retrofit getClient(Context context) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ConnectionPool pool = new ConnectionPool(5, 10000, TimeUnit.MILLISECONDS);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.connectionPool(pool);
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;
    }


    public static Retrofit getClientWithHeader(Context ctx, String token) {
        SharedPref pref = new SharedPref(ctx);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)

                        .addHeader("user-token", (pref != null) ? pref.getString(Constants.SESSION_TOKEN) : "")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        httpClient.connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;
    }


    public static Retrofit getClientWithHeaderLogin(Context ctx, String token, String deviceID, String username) {
        SharedPref pref = new SharedPref(ctx);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        int sdkVersion = Build.VERSION.SDK_INT;
        Log.d("OSversion##", "" + sdkVersion);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("user-token", pref.getString(Constants.SESSION_TOKEN))
                        .addHeader("_di", deviceID)
                        .addHeader("_un", username)
                        .addHeader("_osg", "" + sdkVersion)

                        .build();
                return chain.proceed(newRequest);
            }
        });
        httpClient.connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.dispatcher(dispatcher);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;

    }
}
