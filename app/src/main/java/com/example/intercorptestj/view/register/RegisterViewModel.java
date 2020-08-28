package com.example.intercorptestj.view.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.intercorptestj.model.pojo.User;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;
import com.example.intercorptestj.model.repository.OnResponse;
import com.example.intercorptestj.util.ScreenState;
import com.example.intercorptestj.util.base.BaseViewModel;

import java.util.List;

public class RegisterViewModel extends BaseViewModel {

    private FirebaseRepositoryContract firebaseRepository;

    private MutableLiveData<Boolean> userCreated = new MutableLiveData<>();

    public RegisterViewModel(FirebaseRepositoryContract firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public LiveData<Boolean> setUser(User user) {

        screenStateLiveData.postValue(ScreenState.LOADING);
        firebaseRepository.setUser(user, new OnResponse<Boolean>() {
            @Override
            public void onSuccesfull(Boolean item, List<Boolean> itemList) {
                userCreated.postValue(true);
                screenStateLiveData.postValue(ScreenState.RENDER);

            }

            @Override
            public void onError(String err) {
                userCreated.postValue(false);
                errorLiveData.postValue(err);
                screenStateLiveData.postValue(ScreenState.ERROR);
            }
        });

        return userCreated;
    }

}
