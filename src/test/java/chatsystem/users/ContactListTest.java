package chatsystem.users;

import chatsystem.Main;
import chatsystem.network.UDPSender;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ContactListTest {

    interface FallibleCode {
         void run() throws Exception;
    }

    /** Helper function that checks that the provided piece of code throws an exception.*/
    private static void assertThrows(FallibleCode code) {
        try {
            code.run();
            throw new RuntimeException("The executed code did not throw an exception");
        } catch (Exception e) {
            // ok, an exception was thrown
        }
    }

    @Before
    public void clearContactList() {
        // before each test, reset the contact list (that might have been updated by other tests
        ContactList.getInstance().clear();
    }

    /** Check that new users are correctly added to the list and that duplicated entries are identified. */
    @Test
    public void userAdditionTest() throws ContactAlreadyExists {
        ContactList users = ContactList.getInstance();

        assert !users.hasUsername("toto");
        users.addUser(new Contact("toto"));
        assert users.getUsers().size() == 1;
        assert users.hasUsername("toto");

        assertThrows(() -> users.addUser(new Contact("toto")));

        assert !users.hasUsername("titi");
        users.addUser(new Contact("titi"));
        assert users.getUsers().size() == 2;
        assert users.hasUsername("titi");
        assert users.hasUsername("toto");

        assertThrows(() -> users.addUser(new Contact("toto")));
        assertThrows(() -> users.addUser(new Contact("titi")));

        assert users.getUsers().size() == 2;
    }

    private static final int PORT = 1968;

    /** Tests that the user list updated from UDP messages works as expected. */
    @Test
    public void testListUserUpdate() throws IOException, InterruptedException {
        List<String> usernames = Arrays.asList("alice", "bob", "chloe");

        Main.launchConnectionListener(PORT);
        ContactList users = ContactList.getInstance();

        for(String user : usernames) {
            // check that the user is not already present
            assert !users.hasUsername(user);

            // simulate connection message from remote
            UDPSender.sendLocalhost(PORT, user);
            // make sure the message is processed
            Thread.sleep(300);

            // check that the username is now in the list
            assert users.hasUsername(user);
            System.out.println(users.getUsers());
        }

        // check that all users are present
        assert users.getUsers().size() == usernames.size();
    }
}
