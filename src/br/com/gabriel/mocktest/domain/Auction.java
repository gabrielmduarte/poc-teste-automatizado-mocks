package br.com.gabriel.mocktest.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Auction {

	private String description;
	private Calendar date;
	private List<Bid> bids;
	private boolean ended;
	private int id;
	
	public Auction(String description) {
		this(description, Calendar.getInstance());
	}
	
	public Auction(String description, Calendar date) {
		this.description = description;
		this.date = date;
		this.bids = new ArrayList<Bid>();
	}
	
	public void propose(Bid bid) {
		if(bids.isEmpty() || canMakeBid(bid.getUser())) {
			bids.add(bid);
		}
	}

	private boolean canMakeBid(User user) {
		return !lastBidDone().getUser().equals(user) && numberOfBidsOf(user) <5;
	}

	private int numberOfBidsOf(User user) {
		int total = 0;
		for(Bid l : bids) {
			if(l.getUser().equals(user)) total++;
		}
		return total;
	}

	private Bid lastBidDone() {
		return bids.get(bids.size()-1);
	}

	public String getDescription() {
		return description;
	}

	public List<Bid> getBids() {
		return Collections.unmodifiableList(bids);
	}

	public Calendar getDate() {
		return (Calendar) date.clone();
	}

	public void ends() {
		this.ended = true;
	}
	
	public boolean isEnded() {
		return ended;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
