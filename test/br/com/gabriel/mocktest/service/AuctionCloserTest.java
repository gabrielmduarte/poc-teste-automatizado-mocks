package br.com.gabriel.mocktest.service;

import br.com.gabriel.mocktest.builder.AuctionCreator;
import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.EmailSender;
import br.com.gabriel.mocktest.repository.AuctionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Calendar;
import java.util.List;
import static java.util.List.of;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuctionCloserTest {

    private Calendar old;

    private Calendar yesterday;

    private AuctionRepository fakeDao;

    private EmailSender fakeSender;

    private AuctionCloser auctionCloser;


    @Before
    public void before() {
        this.old = Calendar.getInstance();
        old.set(1999, 1, 20);

        this.yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        this.fakeDao = fakeDao = mock(AuctionRepository.class);
        this.fakeSender = mock(EmailSender.class);

        this.auctionCloser = new AuctionCloser(fakeDao, fakeSender);
    }

    @Test
    public void shouldEndAuctionThatStartedOneWeekAgo() {

        Auction auction = new AuctionCreator().to("Macbook Pro").inDate(old).build();
        Auction auction2 = new AuctionCreator().to("Impressora laser").inDate(old).build();
        List<Auction> oldAuctions = List.of(auction, auction2);

        when(fakeDao.currents()).thenReturn(oldAuctions);

        auctionCloser.close();

        assertEquals(2, auctionCloser.getTotalEnded());
        assertTrue(auction.isEnded());
        assertTrue(auction2.isEnded());
    }

    @Test
    public void shouldNotEndAuctionThatStartedYesterday() {
        Auction auction = new AuctionCreator().to("Macbook Pro").inDate(yesterday).build();
        Auction auction2 = new AuctionCreator().to("Impressora laser").inDate(yesterday).build();

        when(fakeDao.currents()).thenReturn(of(auction, auction2));

        auctionCloser.close();

        assertEquals(0, auctionCloser.getTotalEnded());
        assertFalse(auction.isEnded());
        assertFalse(auction2.isEnded());
    }

    @Test
    public void mustNotCloseAuctionsIfNone() {
        when(fakeDao.currents()).thenReturn(List.of());

        auctionCloser.close();

        assertEquals(0, auctionCloser.getTotalEnded());
    }

    @Test
    public void mustUpdateFinishedAuction() {
        Auction auction = new AuctionCreator().to("Macbook Pro").inDate(old).build();

        when(fakeDao.currents()).thenReturn(List.of(auction));

        auctionCloser.close();

        verify(fakeDao, times(1)).update(auction);
    }

    @Test
    public void mustNotUpdateOpenAuction() {
        Auction auction = new AuctionCreator().to("Macbook Pro").inDate(yesterday).build();

        when(fakeDao.currents()).thenReturn(List.of(auction));

        auctionCloser.close();

        assertEquals(0, auctionCloser.getTotalEnded());
        assertFalse(auction.isEnded());
        verify(fakeDao, never()).update(auction);
    }

    @Test
    public void mustSendEmailAfterPersistEndedAuction() {
        Auction auction = new AuctionCreator().to("TV de plasma").inDate(old).build();

        when(fakeDao.currents()).thenReturn(List.of(auction));

        auctionCloser.close();

        InOrder inOrder = inOrder(fakeDao, fakeSender);
        inOrder.verify(fakeDao, times(1)).update(auction);
        inOrder.verify(fakeSender, times(1)).send(auction);
    }

    @Test
    public void mustContinueExecutionEvenWhenDaoFail() {
        Auction auction = new AuctionCreator().to("Playstation 5").inDate(old).build();
        Auction auction2 = new AuctionCreator().to("iPhone 12").inDate(old).build();

        when(fakeDao.currents()).thenReturn(List.of(auction, auction2));

        doThrow(new RuntimeException()).when(fakeDao).update(auction);

        auctionCloser.close();

        verify(fakeSender, times(0)).send(auction);
        verify(fakeDao).update(auction2);
        verify(fakeSender).send(auction2);
    }

    @Test
    public void mustNotSendEmailWhenDaoFail() {
        Auction auction = new AuctionCreator().to("Playstation 5").inDate(old).build();
        Auction auction2 = new AuctionCreator().to("iPhone 12").inDate(old).build();

        when(fakeDao.currents()).thenReturn(List.of(auction, auction2));

        doThrow(new RuntimeException()).when(fakeDao).update(auction);
        doThrow(new RuntimeException()).when(fakeDao).update(auction2);

        auctionCloser.close();

        verify(fakeSender, never()).send(auction);
        verify(fakeSender, never()).send(auction2);
    }

}
