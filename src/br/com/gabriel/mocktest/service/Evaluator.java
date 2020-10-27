package br.com.gabriel.mocktest.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.gabriel.mocktest.domain.Bid;
import br.com.gabriel.mocktest.domain.Auction;

public class Evaluator {

	private double highestBid = Double.NEGATIVE_INFINITY;
	private double lowestBid = Double.POSITIVE_INFINITY;
	private List<Bid> highestsBids;

	public void evaluate(Auction auction) {
		
		if(auction.getBids().size() == 0) {
			throw new RuntimeException("Isn't possible evaluate an auction without bids!");
		}
		
		for(Bid bid : auction.getBids()) {
			if(bid.getAmount() > highestBid) highestBid = bid.getAmount();
			if (bid.getAmount() < lowestBid) lowestBid = bid.getAmount();
		}
		
		threeHighests(auction);
	}

	private void threeHighests(Auction auction) {
		highestsBids = new ArrayList<Bid>(auction.getBids());
		Collections.sort(highestsBids, new Comparator<Bid>() {

			public int compare(Bid o1, Bid o2) {
				if(o1.getAmount() < o2.getAmount()) return 1;
				if(o1.getAmount() > o2.getAmount()) return -1;
				return 0;
			}
		});
		highestsBids = highestsBids.subList(0, Math.min(highestsBids.size(), 3));
	}

	public List<Bid> getHighestsThree() {
		return highestsBids;
	}
	
	public double getHighestBid() {
		return highestBid;
	}
	
	public double getLowestBid() {
		return lowestBid;
	}
}
