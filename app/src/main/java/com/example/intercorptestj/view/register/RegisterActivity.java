package com.example.intercorptestj.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.example.intercorptestj.databinding.ActivityRegisterBinding;
import com.example.intercorptestj.model.pojo.User;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;
import com.example.intercorptestj.util.base.BaseActivity;
import com.example.intercorptestj.view.datepicker.DatePickerDialogFragment;
import com.example.intercorptestj.view.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ConstraintLayout view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        RegisterViewModelFactory viewModelFactory = new RegisterViewModelFactory(new FirebaseRepositoryImpl());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel.class);

        init();
        initListener();
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

    private void initListener() {
        binding.fabContinue.setOnClickListener(view -> {
            if (validateForm()) {

                try {
                    //create user
                    User user = new User();
                    user.setUuid(firebaseUser.getUid());
                    user.setFirstName(Objects.requireNonNull(binding.textInputEditTextFirstName.getText()).toString());
                    user.setLastName(Objects.requireNonNull(binding.textInputEditTextLastName.getText()).toString());

                    Date birthdate = sdf.parse(Objects.requireNonNull(binding.textInputEditTextBirthdate.getText()).toString());
                    user.setBirthdate(birthdate);

                    viewModel.setUser(user).observe(this, aBoolean -> {
                        if (aBoolean) {
                            goToMain();
                        } else {
                            Toast.makeText(this, "We sorry something is wrong, try later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "We sorry something is not working", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Complete the fields correctly", Toast.LENGTH_SHORT).show();
            }
        });

        binding.textInputEditTextBirthdate.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                DatePickerDialogFragment dialog = new DatePickerDialogFragment(new DatePickerDialogFragment.DatePickerListener() {
                    @Override
                    public void onSelected(Date date) {
                        binding.textInputEditTextBirthdate.setText(sdf.format(date));
                    }
                });
                dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
            }
            return true;
        });

    }

    private Boolean validateForm() {
        Boolean result = true;

        while (true) {

            if (Objects.requireNonNull(binding.textInputEditTextFirstName.getText()).toString().equals("")) {
                binding.textInputEditTextFirstName.setError("required field");
                result = false;
                break;
            }

            if (Objects.requireNonNull(binding.textInputEditTextLastName.getText()).toString().equals("")) {
                binding.textInputEditTextLastName.setError("required field");
                result = false;
                break;
            }

            if (Objects.requireNonNull(binding.textInputEditTextBirthdate.getText()).toString().equals("")) {
                binding.textInputEditTextBirthdate.setError("required field");
                result = false;
                break;
            }

            try {
                Date date = sdf.parse(binding.textInputEditTextBirthdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                result = false;
                break;
            }

            break;
        }
        return result;
    }

    private void goToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}