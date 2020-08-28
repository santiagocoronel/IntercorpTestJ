package com.example.intercorptestj;

import android.app.Application;

import com.facebook.FacebookSdk;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.setApplicationId("681979879196001");
        FacebookSdk.sdkInitialize(this);
    }
}
