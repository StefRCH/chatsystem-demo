package chatsystem.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    private static Logger LOGGER = LogManager.getLogger(UserList.class);

    // The ONLY instance of UserList
    private static final UserList instance = new UserList();

    public static UserList getInstance() {
        return instance;
    }

    /** Private constructor to prevent anybody from invoking it. */
    private UserList() {}

    List<User> users = new ArrayList<>();

    public synchronized void addUser(User user) {
        this.users.add(user);
        LOGGER.info("Added user: {}", user);
    }

    public synchronized boolean hasUsername(String username) {
        return this.users.stream().anyMatch((user) -> user.name.equals(username));
    }

    public synchronized List<User> getUsers() {
        // provide a copy of the list to avoid any modification from external users.
        return new ArrayList<>(this.users);
    }


}
