package br.com.gabriel.mocktest.infra.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.repository.AuctionRepository;

public class AuctionDaoFalso implements AuctionRepository {

	private static List<Auction> auctions = new ArrayList<Auction>();;
	
	public void save(Auction auction) {
		auctions.add(auction);
	}

	@Override
	public List<Auction> finished() {
		return null;
	}

	public List<Auction> finisheds() {
		
		List<Auction> filtereds = new ArrayList<Auction>();
		for(Auction auction : auctions) {
			if(auction.isEnded()) filtereds.add(auction);
		}

		return filtereds;
	}
	
	public List<Auction> currents() {
		
		List<Auction> filtereds = new ArrayList<Auction>();
		for(Auction auction : auctions) {
			if(!auction.isEnded()) filtereds.add(auction);
		}

		return filtereds;
	}
	
	public void update(Auction auction) { /* faz nada! */ }
}
