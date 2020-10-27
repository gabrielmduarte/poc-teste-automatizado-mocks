package br.com.gabriel.mocktest.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;

import java.util.List;

import br.com.gabriel.mocktest.domain.Auction;
import org.junit.Before;
import org.junit.Test;

import br.com.gabriel.mocktest.builder.AuctionCreator;
import br.com.gabriel.mocktest.domain.Bid;
import br.com.gabriel.mocktest.domain.User;

public class EvaluatorTest {
	
	private Evaluator evaluator;
	private User biel;
	private User guina;
	private User ines;

	@Before
	public void criaAvaliador() {
		this.evaluator = new Evaluator();
		this.ines = new User("Inês");
		this.guina = new User("Agnaldo");
		this.biel = new User("Gabriel");
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldNotEvaluateAuctionWithoutBid() {
		Auction auction = new AuctionCreator().to("Playstation 3 Novo").build();
		
		evaluator.evaluate(auction);
		
	}
	
    @Test
    public void mustGetBidsInAscendingOrder() {
        // parte 1: cenario
         
        Auction auction = new Auction("Playstation 3 Novo");
         
        auction.propose(new Bid(ines, 250.0));
        auction.propose(new Bid(guina, 300.0));
        auction.propose(new Bid(biel, 400.0));
         
        // parte 2: acao
        evaluator.evaluate(auction);
         
        // parte 3: validacao
        assertThat(evaluator.getHighestBid(), equalTo(400.0));
        assertThat(evaluator.getLowestBid(), equalTo(250.0));
    }
 
    @Test
    public void shouldGetAuctionWithOnlyOneBid() {
    	User joao = new User("Jo�o");
        Auction auction = new Auction("Playstation 3 Novo");
         
        auction.propose(new Bid(joao, 1000.0));
         
        evaluator.evaluate(auction);
         
        assertEquals(1000.0, evaluator.getHighestBid(), 0.00001);
        assertEquals(1000.0, evaluator.getLowestBid(), 0.00001);
    }
     
    @Test
    public void shouldFoundThreeHighestBids() {
        
        Auction auction = new AuctionCreator().to("Playstation 3")
        		.bid(ines, 100.0)
        		.bid(biel, 200.0)
        		.bid(ines, 300.0)
        		.bid(biel, 400.0)
        		.build();
         
        evaluator.evaluate(auction);
         
        List<Bid> maiores = evaluator.getHighestsThree();
        assertEquals(3, maiores.size());
        
        assertThat(maiores, hasItems(
        		new Bid(biel, 400),
        		new Bid(ines, 300),
        		new Bid(biel, 200)
        ));
        
    }
     
}
