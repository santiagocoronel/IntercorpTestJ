package com.example.intercorptestj.di;

import android.app.Application;

import com.example.intercorptestj.di.module.RepositoryModule;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.view.login.LoginActivity;
import com.example.intercorptestj.view.main.MainActivity;
import com.example.intercorptestj.view.register.RegisterActivity;
import com.example.intercorptestj.view.splash.SplashActivity;

import dagger.Component;

@AppScope
@Component(modules = {AppModule.class, RepositoryModule.class})
public interface AppComponent {

    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(MainActivity activity);

    Application getApplication();

    //region repositories
    FirebaseRepositoryContract firebaseRepositoryContract();
    //endregion
}
