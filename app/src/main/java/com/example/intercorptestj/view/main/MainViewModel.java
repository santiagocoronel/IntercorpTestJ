package com.example.intercorptestj.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.intercorptestj.model.pojo.User;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.OnResponse;
import com.example.intercorptestj.util.ScreenState;
import com.example.intercorptestj.util.base.BaseViewModel;

import java.util.List;

public class MainViewModel extends BaseViewModel {

    private FirebaseRepositoryContract firebaseRepository;
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public MainViewModel(FirebaseRepositoryContract firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public LiveData<User> getUser(String uuid) {
        screenStateLiveData.postValue(ScreenState.LOADING);
        firebaseRepository.getUser(uuid, new OnResponse<User>() {
            @Override
            public void onSuccesfull(User item, List<User> itemList) {
                screenStateLiveData.postValue(ScreenState.RENDER);
                userMutableLiveData.postValue(item);
            }
            @Override
            public void onError(String err) {
                userMutableLiveData.postValue(null);
                errorLiveData.postValue(err);
                screenStateLiveData.postValue(ScreenState.ERROR);
            }
        });
        return userMutableLiveData;
    }
}
