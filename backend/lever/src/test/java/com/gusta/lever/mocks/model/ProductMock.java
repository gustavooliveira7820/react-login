package com.gusta.lever.mocks.model;

import com.gusta.lever.model.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductMock {
    public static Product generateMock(int number) {
        return new Product(UUID.randomUUID(), "x", "x", "x", "x", "x", 1.1, 10, LocalDate.MIN, "a", false);
    }

    public static List<Product> generateMockList(int amount) {
        ArrayList<Product> mocks = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            mocks.add(generateMock(i));
            if (i % 2 == 0) {
                mocks.get(i).setCollected(true);
            }
        }
        return mocks;
    }
}
