package chatsystem.network;

import chatsystem.network.Message;
import chatsystem.network.UDPServer;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class UDPServerTest {

    private static final int PORT = 1871;


    @Test
    public void mainTest() throws IOException, InterruptedException {
        // message that we will send
        List<String> messages = Arrays.asList("foo", "bar");

        // list where received messages will be added
        List<String> received = new ArrayList<>();

        Consumer<Message> subscriber = (msg) -> {
            System.out.println("received: "+msg);
            received.add(msg.text);
        };
        UDPServer server = new UDPServer(PORT);
        server.addSubscriber(subscriber);

        // launch server
        server.start();

        // send all test messages
        for(String msg : messages) {
            UDPSender.sendLocalhost(PORT, msg);
        }
        Thread.sleep(3000);
        System.out.println(received);

        // check that we have received all test messages
        assert received.size() == messages.size();
        assert messages.equals(received);

    }

}
