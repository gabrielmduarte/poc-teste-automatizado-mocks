package br.com.gabriel.mocktest.repository;

import br.com.gabriel.mocktest.domain.Payment;

public interface PaymentRepository {

    void save(Payment payment);
}
