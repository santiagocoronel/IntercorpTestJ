package com.example.intercorptestj.model.repository;

import com.example.intercorptestj.model.pojo.User;

public interface FirebaseRepositoryContract {

    void getUser(String uuid, OnResponse<User> onResponse);

    void setUser(User user, OnResponse<Boolean> onResponse);

}
