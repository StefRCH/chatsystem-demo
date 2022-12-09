package chatsystem.users;

import java.util.Objects;

/** A remote user, currently only characterized by its name. */
public class Contact {

    final String name;

    public Contact(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact user = (Contact) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                '}';
    }
}
