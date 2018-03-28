package com.woowahan.goosgbt_study.auctionsniper;

import org.jooq.lambda.Unchecked;

import static com.woowahan.goosgbt_study.auctionsniper.Main.MainWindow.*;

public class ApplicationRunner
{
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction)
    {
        Thread thread = new Thread(() -> {
            Unchecked.runnable(() ->
                    Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId())
            );
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
