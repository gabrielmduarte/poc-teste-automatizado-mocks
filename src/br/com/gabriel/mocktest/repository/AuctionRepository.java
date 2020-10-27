package br.com.gabriel.mocktest.repository;

import br.com.gabriel.mocktest.domain.Auction;

import java.util.List;

public interface AuctionRepository {

    void save(Auction auction);

    List<Auction> finished();

    List<Auction> currents();

    void update(Auction auction);

}
