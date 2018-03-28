package com.woowahan.goosgbt_study.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Main
{
    public static final String MAIN_WINDOW_NAME = "Auction Sniper";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    private MainWindow ui;

    public Main() throws Exception
    {
        startUserInterface();
    }

    public static void main(String... args) throws Exception
    {
        Main main = new Main();
        main.joinAuction(
                connecTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),
                args[ARG_ITEM_ID]);

    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException
    {
        Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                (aChat, message) -> {
                    SwingUtilities.invokeLater(() -> {
                            ui.showStatus(MainWindow.STATUS_LOST);
                    });
                }
        );
        chat.sendMessage(new Message());
    }

    private static XMPPConnection connecTo(String hostname, String username, String password) throws XMPPException
    {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection)
    {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private void startUserInterface() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    public static class MainWindow extends JFrame
    {
        public static final String STATUS_JOINING = "joining";
        public static final String STATUS_LOST = "lost";

        public static final String SNIPER_STATUS_NAME = "sniper status";

        private final JLabel sniperStatus = createLabel(STATUS_JOINING);

        public MainWindow()
        {
            super("Auction Sniper");
            setName(MAIN_WINDOW_NAME);
            add(sniperStatus);
            pack();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private static JLabel createLabel(String initialText)
        {
            JLabel label= new JLabel(initialText);
            label.setName(SNIPER_STATUS_NAME);
            label.setBorder(new LineBorder(Color.BLACK));
            return label;
        }

        public void showStatus(String status)
        {
            sniperStatus.setText(status);
        }
    }
}
