package com.example.intercorptestj.model.repository;

import java.util.List;

public interface OnResponse<T> {
    void onSuccesfull(T item, List<T> itemList);
    void onError(String err);
}
