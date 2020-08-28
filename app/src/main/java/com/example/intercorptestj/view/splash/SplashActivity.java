package com.example.intercorptestj.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.intercorptestj.R;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;
import com.example.intercorptestj.util.base.BaseActivity;
import com.example.intercorptestj.view.login.LoginActivity;
import com.example.intercorptestj.view.main.MainActivity;
import com.example.intercorptestj.view.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends BaseActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashViewModelFactory viewModelFactory = new SplashViewModelFactory(new FirebaseRepositoryImpl());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel.class);

        new Handler().postDelayed(
                () -> init(), 1000
        );
    }

    private void init() {
        verifySession();

    }

    private void verifySession() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            viewModel.getUser(firebaseUser.getUid()).observe(this, user -> {
                if (user == null) {
                    goToRegister();
                } else {
                    goToMain();
                }
            });
        } else {
            goToLogin();
        }
    }

    private void goToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToRegister() {
        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}