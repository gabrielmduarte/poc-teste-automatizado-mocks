package br.com.gabriel.mocktest.domain;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class AuctionTest {

	@Test
	public void mustReceiveOneBid() {
		Auction auction = new Auction("Macbook Pro 15");
		assertEquals(0, auction.getBids().size());
		
		auction.propose(new Bid(new User("Steve Jobs"), 2000));
		
		assertEquals(1, auction.getBids().size());
		assertEquals(2000, auction.getBids().get(0).getAmount(), 0.00001);
	}
	
	@Test
	public void mustReceiveSeveralBids() {
		Auction auction = new Auction("Macbook Pro 15");
		auction.propose(new Bid(new User("Steve Jobs"), 2000));
		auction.propose(new Bid(new User("Steve Wozniak"), 3000));
		
		assertEquals(2, auction.getBids().size());
		assertEquals(2000.0, auction.getBids().get(0).getAmount(), 0.00001);
		assertEquals(3000.0, auction.getBids().get(1).getAmount(), 0.00001);
	}
	
	@Test
	public void shouldNotReceiceTwoBidsFromTheSameUserInARow() {
		User steveJobs = new User("Steve Jobs");
		Auction auction = new Auction("Macbook Pro 15");
		auction.propose(new Bid(steveJobs, 2000));
		auction.propose(new Bid(steveJobs, 3000));
		
		assertEquals(1, auction.getBids().size());
		assertEquals(2000.0, auction.getBids().get(0).getAmount(), 0.00001);
	}
	
	@Test
	public void shouldNotAcepptMoreThanFiveBidsFromTheSameUser() {
		User steveJobs = new User("Steve Jobs");
		User billGates = new User("Bill Gates");

		Auction auction = new Auction("Macbook Pro 15");
		auction.propose(new Bid(steveJobs, 2000));
		auction.propose(new Bid(billGates, 3000));
		auction.propose(new Bid(steveJobs, 4000));
		auction.propose(new Bid(billGates, 5000));
		auction.propose(new Bid(steveJobs, 6000));
		auction.propose(new Bid(billGates, 7000));
		auction.propose(new Bid(steveJobs, 8000));
		auction.propose(new Bid(billGates, 9000));
		auction.propose(new Bid(steveJobs, 10000));
		auction.propose(new Bid(billGates, 11000));
		auction.propose(new Bid(steveJobs, 12000));
		
		assertEquals(10, auction.getBids().size());
		assertEquals(11000.0, auction.getBids().get(auction.getBids().size()-1).getAmount(), 0.00001);
	}	
}
