package chatsystem.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UDPServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class);
    private final DatagramSocket socket;

    /** List of subscriber that should be notified of new messages. */
    List<Consumer<Message>> subscribers = new ArrayList<>();

    public UDPServer(int port) throws SocketException {
        // if this fails (SocketException), the exception is non-recoverable and is propagated
        socket = new DatagramSocket(port);
    }

    public void addSubscriber(Consumer<Message> subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                LOGGER.trace("Received ({}): {}", socket.getLocalPort(), received);
                Message msg = new Message(received);
                for (Consumer<Message> subscriber : subscribers) {
                    subscriber.accept(msg);
                }

            } catch (Exception e) {
                // non fatal error, log but continue processing
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }



}
