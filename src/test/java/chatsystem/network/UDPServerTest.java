package chatsystem.network;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UDPServerTest {

    private static final int PORT = 1871;


    @Test
    public void sendReceiveTest() throws IOException, InterruptedException {
        // message that we will send
        List<String> messages = Arrays.asList("foo", "bar");

        // list where received messages will be added
        List<String> received = new ArrayList<>();

        // create server and add a subscriber that will add each received message to our
        // reception list
        UDPServer server = new UDPServer(PORT);
        server.addSubscriber((msg) -> {
            System.out.println("received: "+msg);
            received.add(msg.text);
        });

        // launch server
        server.start();
        Thread.sleep(100); // make sure the thread has time to start up

        // send all test messages
        for(String msg : messages) {
            UDPSender.sendLocalhost(PORT, msg);
        }
        Thread.sleep(100);
        System.out.println("Received messages: " + received);

        // check that we have received all test messages
        assert received.size() == messages.size();
        assert messages.equals(received);

    }

}
