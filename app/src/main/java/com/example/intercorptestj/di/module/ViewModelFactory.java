package com.example.intercorptestj.di.module;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory<T> implements ViewModelProvider.Factory {

    private Provider<T> viewModelProvider;

    @Inject
    public ViewModelFactory(Provider<T> viewModelProvider) {
        this.viewModelProvider = viewModelProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return ((T) viewModelProvider.get());
    }
}
