package br.com.gabriel.mocktest.service;

import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.Clock;
import br.com.gabriel.mocktest.domain.Payment;
import br.com.gabriel.mocktest.domain.SystemClock;
import br.com.gabriel.mocktest.repository.AuctionRepository;
import br.com.gabriel.mocktest.repository.PaymentRepository;

import java.util.Calendar;
import java.util.List;

public class PaymentGenerator {

    private final AuctionRepository auctions;
    private final Evaluator evaluator;
    private final PaymentRepository payments;
    private final Clock clock;

    public PaymentGenerator(AuctionRepository auctions,
                            PaymentRepository payments,
                            Evaluator evaluator,
                            Clock clock) {
        this.auctions = auctions;
        this.evaluator = evaluator;
        this.payments = payments;
        this.clock = clock;
    }

    public PaymentGenerator(AuctionRepository auctions,
                            PaymentRepository payments,
                            Evaluator evaluator) {
        this(auctions, payments, evaluator, new SystemClock());
    }

    public void generate() {
        List<Auction> finishedAuctions = this.auctions.finished();

        for (Auction auction : finishedAuctions) {
            this.evaluator.evaluate(auction);

            Payment newPayment = new Payment(evaluator.getHighestBid(), getFirstWorkday());
            this.payments.save(newPayment);
        }
    }

    private Calendar getFirstWorkday() {
        Calendar date = clock.today();
        int weekday = date.get(Calendar.DAY_OF_WEEK);

        if (weekday == Calendar.SATURDAY) date.add(Calendar.DAY_OF_MONTH, 2);
        if (weekday == Calendar.SUNDAY) date.add(Calendar.DAY_OF_MONTH, 1);

        return date;
    }

}
