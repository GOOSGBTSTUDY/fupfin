package com.woowahan.goosgbt_study.auctionsniper;

import io.vavr.control.Try;

import static com.woowahan.goosgbt_study.auctionsniper.Main.MainWindow.*;

public class ApplicationRunner
{
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction)
    {
        Thread thread = new Thread(() -> {
            Try.run(() ->
                    Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId()))
                    .onFailure((t) -> t.printStackTrace());
        }, "Test Application");
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(STATUS_JOINING);
    }

    public void showsSniperHasLostAuction()
    {
        driver.showsSniperStatus(STATUS_LOST);
    }

    public void stop()
    {
        if(driver != null)
            driver.dispose();
    }
}
