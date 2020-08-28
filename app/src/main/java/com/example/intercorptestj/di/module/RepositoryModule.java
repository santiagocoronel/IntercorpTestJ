package com.example.intercorptestj.di.module;

import com.example.intercorptestj.di.AppScope;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.FirebaseRepositoryImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @AppScope
    FirebaseRepositoryContract provideFirebaseRepository(){
        return new FirebaseRepositoryImpl();
    }
}
