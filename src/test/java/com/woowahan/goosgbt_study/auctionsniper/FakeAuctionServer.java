package com.woowahan.goosgbt_study.auctionsniper;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;


public class FakeAuctionServer
{
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";

    private static final String AUCTION_PASSWROD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId)
    {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException
    {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWROD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                (chat, createdLocally) -> keep(chat).addMessageListener(messageListener));
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException
    {
        messageListener.receivesMessage();
    }

    public void announceClosed() throws XMPPException
    {
        currentChat.sendMessage(new Message());
    }

    public void stop()
    {
        connection.disconnect();
    }

    public String getItemId()
    {
        return itemId;
    }

    private Chat keep(Chat chat)
    {
        return this.currentChat = chat;
    }

    public class SingleMessageListener implements MessageListener
    {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

        @Override
        public void processMessage(Chat chat, Message message)
        {
            messages.add(message);
        }

        public void receivesMessage() throws InterruptedException
        {
            assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
        }
    }
}
