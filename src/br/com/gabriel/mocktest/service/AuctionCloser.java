package br.com.gabriel.mocktest.service;

import java.util.Calendar;
import java.util.List;

import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.EmailSender;
import br.com.gabriel.mocktest.infra.dao.AuctionDao;
import br.com.gabriel.mocktest.repository.AuctionRepository;

public class AuctionCloser {

	private int total = 0;
	private final AuctionRepository dao;
	private final EmailSender emailSender;


	public AuctionCloser(AuctionRepository repository, EmailSender emailSender) {
		this.dao = repository;
		this.emailSender = emailSender;
	}

	public void close() {
		List<Auction> allCurrentAuctions = dao.currents();

		for (Auction auction : allCurrentAuctions) {
			try {
				if (startedLastWeek(auction)) {
					auction.ends();
					total++;
					dao.update(auction);
					emailSender.send(auction);
				}
			} catch (Exception e) {}
		}
	}

	private boolean startedLastWeek(Auction auction) {
		return daysBetween(auction.getDate(), Calendar.getInstance()) >= 7;
	}

	private int daysBetween(Calendar inicio, Calendar fim) {
		Calendar date = (Calendar) inicio.clone();
		int daysInBetween = 0;
		while (date.before(fim)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysInBetween++;
		}

		return daysInBetween;
	}

	public int getTotalEnded() {
		return total;
	}
}
