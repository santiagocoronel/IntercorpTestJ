package com.example.intercorptestj.view.login;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.example.intercorptestj.App;
import com.example.intercorptestj.databinding.ActivityLoginBinding;
import com.example.intercorptestj.di.AppComponent;
import com.example.intercorptestj.di.DaggerAppComponent;
import com.example.intercorptestj.di.module.ViewModelFactory;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;
import com.example.intercorptestj.util.ScreenState;
import com.example.intercorptestj.util.base.BaseActivity;
import com.example.intercorptestj.view.main.MainActivity;
import com.example.intercorptestj.view.register.RegisterActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CallbackManager mCallbackManager;
    private ActivityLoginBinding binding;
    private Boolean isStateEnterPhoneNumber = true;
    private ConstraintLayout view;

    @Inject
    FirebaseRepositoryContract firebaseRepository;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent(((App) getApplicationContext()).getAppComponent());
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        LoginViewModelFactory viewModelFactory = new LoginViewModelFactory(firebaseRepository);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        init();
        initFirebaseCallback();
        initLoginFacebook();
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
    }

    private void initListeners() {
        binding.buttonLogin.setOnClickListener(view -> {
            if (isStateEnterPhoneNumber) {
                String phoneNumber = binding.editTextPhoneNumber.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    sendVerificationCode(phoneNumber);
                } else {
                    binding.editTextPhoneNumber.setError("required field");
                }
            } else {
                String code = binding.editTextCode.getText().toString();
                if (!code.isEmpty()) {
                    verifyPhoneNumberWithCode(viewModel.verificationIdLiveData.getValue(), code);
                } else {
                    binding.editTextPhoneNumber.setError("required field");
                }
            }
        });
    }

    public void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void initFirebaseCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    viewModel.errorLiveData.setValue(e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    viewModel.errorLiveData.setValue(e.getMessage());
                }
                viewModel.screenStateLiveData.setValue(ScreenState.ERROR);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                viewModel.verificationIdLiveData.setValue(verificationId);
                viewModel.resendToken.setValue(token);
                showEnterSmsCode();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        retrieveUserData(user);
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            viewModel.errorLiveData.setValue(task.getException().getMessage());
                        }
                        viewModel.screenStateLiveData.setValue(ScreenState.ERROR);
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void retrieveUserData(FirebaseUser firebaseUser) {
        viewModel.screenStateLiveData.setValue(ScreenState.LOADING);
        String uuid = firebaseUser.getUid();
        viewModel.getUser(uuid).observe(this, user -> {
            viewModel.screenStateLiveData.setValue(ScreenState.RENDER);
            if (user == null) {
                goToRegister();
            } else {
                goToMain();
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initLoginFacebook() {
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = binding.buttonFacebook;
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showEnterPhoneNumber();
            }

            @Override
            public void onError(FacebookException error) {
                viewModel.errorLiveData.setValue(error.getMessage());
                viewModel.screenStateLiveData.setValue(ScreenState.ERROR);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        retrieveUserData(user);
                    } else {
                        viewModel.errorLiveData.setValue(task.getException().getMessage());
                        viewModel.screenStateLiveData.setValue(ScreenState.ERROR);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showEnterPhoneNumber() {
        isStateEnterPhoneNumber = true;
        binding.editTextPhoneNumber.setVisibility(View.VISIBLE);
        binding.editTextCode.setVisibility(View.GONE);
        binding.buttonLogin.setText("Send SMS");
    }

    private void showEnterSmsCode() {
        isStateEnterPhoneNumber = false;
        binding.editTextPhoneNumber.setVisibility(View.GONE);
        binding.editTextCode.setVisibility(View.VISIBLE);
        binding.buttonLogin.setText("Login");
    }

    @Override
    protected void initComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }
}
