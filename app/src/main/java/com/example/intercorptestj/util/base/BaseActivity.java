package com.example.intercorptestj.util.base;


import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intercorptestj.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    protected Integer loadingView = R.layout.loading;
    private View mainView;

    protected void showLoading(@NonNull View mainView) {
        this.mainView = mainView;
        setContentView(loadingView);
        ((ProgressBar) findViewById(R.id.progressBar)).setIndeterminate(true);
    }

    protected void hiddenLoading() {
        if (mainView != null) {
            setContentView(mainView);
        }
    }
}
