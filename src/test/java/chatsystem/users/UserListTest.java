package chatsystem.users;

import chatsystem.Main;
import chatsystem.network.UDPSender;
import chatsystem.users.UserList;
import chatsystem.network.UDPServerTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserListTest {

    private static final int PORT = 1968;

    @Test
    public void testListUserUpdate() throws IOException, InterruptedException {
        List<String> usernames = Arrays.asList("EMacron", "EBorne", "GDarmanin");

        Main.launchConnectionListener(PORT);
        UserList users = UserList.getInstance();

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
