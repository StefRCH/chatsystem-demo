package chatsystem.network;

/** Simple text message, without any metadata. */
public class Message {
    /** Text of the message. Public because it is immutable (final + String type). */
    public final String text;

    public Message(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
