package ru.shalaginov.repository;

import ru.shalaginov.enity.Payment;

import java.util.List;

public interface PaymentRepositoryInt {
    Payment create(Payment payment);
    Payment findById(int paymentId);
    List<Payment> findAll();
    Payment update(Payment payment);
    boolean delete(int paymentId);
    boolean existsById(int paymentId);
    double getTotalPaymentsByUserId(int userId);
}
