package com.example.intercorptestj;

import android.app.Application;

import com.example.intercorptestj.di.AppComponent;
import com.example.intercorptestj.di.AppModule;
import com.example.intercorptestj.di.DaggerAppComponent;
import com.facebook.FacebookSdk;


public class App extends Application {

    AppComponent appComponent;
    Application applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        initDagger();

        FacebookSdk.setApplicationId("681979879196001");
        FacebookSdk.sdkInitialize(this);
    }

    private void initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(applicationInstance)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
