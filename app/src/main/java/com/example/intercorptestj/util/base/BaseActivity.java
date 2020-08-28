package com.example.intercorptestj.util.base;


import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intercorptestj.R;
import com.example.intercorptestj.di.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    protected Integer loadingView = R.layout.loading;
    private View mainView;

    protected abstract void initComponent(AppComponent appComponent);

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
