package com.gusta.lever.mocks.dto;

import com.gusta.lever.dto.ProductDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDTOMock {
    public static ProductDTO generateMock(int number) {
        return new ProductDTO(UUID.randomUUID(), "x", "x", "x", "x", "x", 1.1, 10, LocalDate.MIN, "a", false);
    }

    public static List<ProductDTO> generateMockList(int amount) {
        ArrayList<ProductDTO> mocks = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            mocks.add(generateMock(i));
            if (i % 2 == 0) {
                mocks.get(i).setCollected(true);
            }
        }
        return mocks;
    }
}
