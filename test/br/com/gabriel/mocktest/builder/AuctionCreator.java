package br.com.gabriel.mocktest.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.gabriel.mocktest.domain.Bid;
import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.User;

public class AuctionCreator {

	private String description;
	private Calendar date;
	private List<Bid> bids;
	private boolean ended;

	public AuctionCreator() {
		this.date = Calendar.getInstance();
		bids = new ArrayList<Bid>();
	}
	
	public AuctionCreator to(String description) {
		this.description = description;
		return this;
	}
	
	public AuctionCreator inDate(Calendar date) {
		this.date = date;
		return this;
	}

	public AuctionCreator bid(User user, double amount) {
		bids.add(new Bid(user, amount));
		return this;
	}

	public AuctionCreator ended() {
		this.ended = true;
		return this;
	}

	public Auction build() {
		Auction auction = new Auction(description, date);
		for(Bid bidDone : bids) auction.propose(bidDone);
		if(ended) auction.ends();
				
		return auction;
	}

}
