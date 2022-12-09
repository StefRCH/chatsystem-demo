package chatsystem;

import chatsystem.network.UDPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import chatsystem.users.User;
import chatsystem.users.UserList;

import java.net.SocketException;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final int CONNECTION_PORT = 1789;

    public static void main(String[] args)  {
        try {
            launchConnectionListener(CONNECTION_PORT);
        } catch (SocketException e) {
            // error while launching connection listener, abort
            LOGGER.error("Unable to launch connection listener.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** Start the background component in charge of updating the user list. */
    public static void launchConnectionListener(int port) throws SocketException {
        LOGGER.debug("Launching connection listener");

        // shared user list instance
        UserList users = UserList.getInstance();

        // server to be started that will listen for new message
        UDPServer connectionHandler = new UDPServer(port);

        // for each received message, add a corresponding user
        connectionHandler.addSubscriber((message) -> {
            users.addUser(new User(message.text));
        });

        // start background threas
        connectionHandler.setDaemon(true);
        connectionHandler.start();
    }
}
