package com.woowahan.goosgbt_study.auctionsniper;

import org.junit.jupiter.api.*;

class AuctionSniperEndToEndTest
{
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test void sniperJoinsAuctionUntilAuctionCloses() throws Exception
    {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @AfterEach void stopAction()
    {
        auction.stop();
    }

    @AfterEach void stopApplication()
    {
        application.stop();
    }
}
