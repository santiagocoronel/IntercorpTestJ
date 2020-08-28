package com.example.intercorptestj.view.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.intercorptestj.model.pojo.User;
import com.example.intercorptestj.model.repository.FirebaseRepositoryContract;
import com.example.intercorptestj.model.repository.OnResponse;
import com.example.intercorptestj.util.ScreenState;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JUnit4.class)
public class LoginViewModelTest {

    //this rule allows us to run Livedata synchronously
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FirebaseRepositoryContract firebaseRepository;

    @Mock
    Observer<ScreenState> observerScreenState;

    @Mock
    Observer<User> observerUser;

    @Mock
    OnResponse<User> onResponseUser;

    LoginViewModel loginViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginViewModel = new LoginViewModel(firebaseRepository);
        loginViewModel.screenStateLiveData.observeForever(observerScreenState);
        loginViewModel.userMutableLiveData.observeForever(observerUser);


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void searchUserByUUIDReturnNoExistError() {

        String NoExistingUIDD = "Pns0DyI0pnOWaZk2ZD35b0J1nc93NoExist";

        User mockUser = null;
        MutableLiveData<User> mutableLiveData = new MutableLiveData();
        mutableLiveData.postValue(mockUser);

        loginViewModel.userMutableLiveData.postValue(mockUser);
        //BDDMockito.given(loginViewModel.getUser(NoExistingUIDD)).willReturn(mutableLiveData);

        assertNull(loginViewModel.getUser(NoExistingUIDD).getValue());

        Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.RENDER);
        Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.LOADING);
        //Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.ERROR);

        verifyNoMoreInteractions(observerScreenState);
    }

    @Test
    public void searchUserByUUIDReturnExistSuccess() {

        String existingUUID = "Pns0DyI0pnOWaZk2ZD35b0J1nc93";

        User mockUserNotNull = new User();
        mockUserNotNull.setUuid(existingUUID);

        MutableLiveData<User> mutableLiveData = new MutableLiveData();
        mutableLiveData.postValue(mockUserNotNull);

        loginViewModel.userMutableLiveData.postValue(mockUserNotNull);
        //BDDMockito.given(loginViewModel.getUser(existingUUID)).willReturn(mutableLiveData);

        assertNotNull(loginViewModel.getUser(existingUUID).getValue());

        Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.RENDER);
        Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.LOADING);
        //Mockito.verify(observerScreenState, Mockito.times(1)).onChanged(ScreenState.RENDER);

        verifyNoMoreInteractions(observerScreenState);
    }
}