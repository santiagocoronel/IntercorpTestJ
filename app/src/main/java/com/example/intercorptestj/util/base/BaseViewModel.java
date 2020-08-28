package com.example.intercorptestj.util.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.intercorptestj.util.ScreenState;


public abstract class BaseViewModel extends ViewModel {

    public MutableLiveData<ScreenState> screenStateLiveData = new MutableLiveData<>(ScreenState.RENDER);
    public MutableLiveData<String> errorLiveData = new MutableLiveData<>();



}
