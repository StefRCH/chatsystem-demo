package chatsystem;

import chatsystem.network.UDPServer;
import chatsystem.users.ContactAlreadyExists;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import chatsystem.users.Contact;
import chatsystem.users.ContactList;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.SocketException;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /** Port on which connection messages are exchanged. */
    private static final int CONNECTION_PORT = 1789;

    public static void main(String[] args)  {
        Configurator.setRootLevel(Level.INFO); // only show INFO message in the application (debug are ignored)
        LOGGER.info("Starting chatsystem. (connection-port={})", CONNECTION_PORT);
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
        ContactList users = ContactList.getInstance();

        // server to be started that will listen for new message
        UDPServer connectionHandler = new UDPServer(port);

        // for each received message, add a corresponding user
        connectionHandler.addSubscriber((message) -> {
            try {
                users.addUser(new Contact(message.text));
            } catch (ContactAlreadyExists e) {
                LOGGER.error("Failed to add user to list: {}", message);
            }
        });

        // start background thread
        // connectionHandler.setDaemon(true); // uncomment if we do not want this thread to prevent the application from closing
        connectionHandler.start();
    }
}
