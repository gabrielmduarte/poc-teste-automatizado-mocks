package br.com.gabriel.mocktest.service;

import br.com.gabriel.mocktest.builder.AuctionCreator;
import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.Clock;
import br.com.gabriel.mocktest.domain.Payment;
import br.com.gabriel.mocktest.domain.User;
import br.com.gabriel.mocktest.repository.AuctionRepository;
import br.com.gabriel.mocktest.repository.PaymentRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PaymentGeneratorTest {

    @Test
    public void mustGeneratePaymentForAFinishedAuction() {
        AuctionRepository auctions = mock(AuctionRepository.class);
        PaymentRepository payments = mock(PaymentRepository.class);
        Evaluator evaluator = mock(Evaluator.class);

        Auction auction = new AuctionCreator().to("Telefone sem fio")
                .bid(new User("Gabriel Duarte"), 150.0)
                .bid(new User("Maria Odette"), 250.0)
                .build();

        when(auctions.finished()).thenReturn(List.of(auction));
        when(evaluator.getHighestBid()).thenReturn(250.0);

        PaymentGenerator generator = new PaymentGenerator(auctions, payments, evaluator);
        generator.generate();

        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        verify(payments).save(argument.capture());

        Payment generatedPayment = argument.getValue();

        assertEquals(250.0, generatedPayment.getAmount(), 0.00001);
    }

    @Test
    public void mustMovePaymentToNextWorkday() {
        AuctionRepository auctions = mock(AuctionRepository.class);
        PaymentRepository payments = mock(PaymentRepository.class);
        Evaluator evaluator = mock(Evaluator.class);
        Clock clock = mock(Clock.class);

        Auction auction = new AuctionCreator().to("Telefone sem fio")
                .bid(new User("Gabriel Duarte"), 150.0)
                .bid(new User("Maria Odette"), 250.0)
                .build();

        when(auctions.finished()).thenReturn(List.of(auction));

        Calendar saturday = Calendar.getInstance();
        saturday.set(2020, Calendar.OCTOBER, 24);

        when(clock.today()).thenReturn(saturday);

        PaymentGenerator generator = new PaymentGenerator(auctions, payments, evaluator, clock);
        generator.generate();

        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        verify(payments).save(argument.capture());
        Payment generatedPayment = argument.getValue();

        assertEquals(Calendar.MONDAY, generatedPayment.getDate().get(Calendar.DAY_OF_WEEK));
        assertEquals(26, generatedPayment.getDate().get(Calendar.DAY_OF_MONTH));
    }

}
