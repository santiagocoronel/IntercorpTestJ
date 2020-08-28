package com.example.intercorptestj.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.intercorptestj.App;
import com.example.intercorptestj.databinding.ActivityMainBinding;
import com.example.intercorptestj.di.AppComponent;
import com.example.intercorptestj.model.pojo.User;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;
import com.example.intercorptestj.util.base.BaseActivity;
import com.example.intercorptestj.view.splash.SplashActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private ConstraintLayout view;

    @Inject
    FirebaseRepositoryContract firebaseRepository;

    private MainViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent(((App) getApplicationContext()).getAppComponent());
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        MainViewModelFactory viewModelFactory = new MainViewModelFactory(firebaseRepository);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        init();
        initListeners();
    }

    private void init() {
        viewModel.screenStateLiveData.observe(this, screenState -> {
            switch (screenState) {
                case RENDER:
                    //show single render, but in this screen we used others for example enter_phone_number and enter_sms_code
                    hiddenLoading();
                    break;
                case LOADING:
                    //show progressbar
                    showLoading(view);
                    break;
                case ERROR:
                    //show error message
                    break;
                default:

                    break;

            }
        });

        viewModel.getUser(FirebaseAuth.getInstance().getUid()).observe(this, user -> {
            binding.textViewFirstName.setText("First Name: " + user.getFirstName());
            binding.textViewLastName.setText("Last Name: " + user.getLastName());
            binding.textViewBirthdate.setText("Birthdate: " + sdf.format(user.getBirthdate()));
        });
    }

    private void initListeners() {
        binding.buttonLogout.setOnClickListener(view -> {
            logout();
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        goToSplash();
    }

    private void goToSplash() {
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void initComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }
}