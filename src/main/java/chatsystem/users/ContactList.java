package chatsystem.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that provides access to the list of contacts.
 * The unique instance of this class will be accessed by several parts of the program, typically in different threads.
 * For this reason, we must ensure that these concurrent accesses to not lead to race conditions,
 * which is why all methods are synchronized.
 */
public class ContactList {

    private static final Logger LOGGER = LogManager.getLogger(ContactList.class);

    // The ONLY instance of UserList
    private static final ContactList instance = new ContactList();

    public static ContactList getInstance() {
        return instance;
    }

    /** Private constructor to prevent anybody from invoking it. */
    private ContactList() {}

    List<Contact> users = new ArrayList<>();

    /** Add the given contact to the list. Throws an exception if a contact with the same name already exist in the list. */
    public synchronized void addUser(Contact user) throws ContactAlreadyExists {
        if(this.hasUsername(user.name)) {
            LOGGER.error("User name already exists: {}", user);
            throw new ContactAlreadyExists(user.name);
        } else {
            this.users.add(user);
            LOGGER.info("Added user: {}", user);
        }
    }

    public synchronized boolean hasUsername(String username) {
        return this.users.stream().anyMatch((user) -> user.name.equals(username));
    }

    public synchronized List<Contact> getUsers() {
        // provide a copy of the list to avoid any modification or concurrent access from external consumers.
        return new ArrayList<>(this.users);
    }

    public synchronized void clear() {
        this.users.clear();
    }

}
