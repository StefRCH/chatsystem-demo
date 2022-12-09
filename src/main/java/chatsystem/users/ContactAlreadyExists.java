package chatsystem.users;

/** Error message thrown when trying to add an already existing user to the list. */
public class ContactAlreadyExists extends Exception {
    public final String name;

    public ContactAlreadyExists(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserAlreadyExists{" +
                "name='" + name + '\'' +
                '}';
    }
}
