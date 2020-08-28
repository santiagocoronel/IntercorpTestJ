package com.example.intercorptestj.view.splash;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;

import java.lang.reflect.InvocationTargetException;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final FirebaseRepositoryContract firebaseRepository;

    public SplashViewModelFactory(FirebaseRepositoryContract firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(FirebaseRepositoryContract.class)
                    .newInstance(firebaseRepository);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
