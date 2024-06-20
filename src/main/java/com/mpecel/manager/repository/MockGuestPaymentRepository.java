package com.mpecel.manager.repository;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class MockGuestPaymentRepository implements GuestPaymentRepository {

    @Override
    public List<Double> getGuestPayments() {
        return Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0);
    }
}
