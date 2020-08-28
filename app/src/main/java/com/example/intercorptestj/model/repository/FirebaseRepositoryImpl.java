package com.example.intercorptestj.model.repository;

import android.util.Log;

import com.example.intercorptestj.model.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRepositoryImpl implements FirebaseRepositoryContract {

    String TAG = FirebaseRepositoryImpl.class.getSimpleName();

    FirebaseDatabase database;
    DatabaseReference reference;

    public FirebaseRepositoryImpl() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

    }

    @Override
    public void getUser(String uuid, OnResponse<User> onResponse) {
        reference.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                onResponse.onSuccesfull(value, null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
                onResponse.onError(error.getMessage());
            }
        });
    }

    @Override
    public void setUser(User user, OnResponse<Boolean> onResponse) {
        reference.child(user.getUuid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    onResponse.onSuccesfull(true, null);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    onResponse.onError(e.getMessage());
                });
    }
}
