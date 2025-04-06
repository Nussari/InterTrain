package vv.intertrain.vinnsla;

public class Message {
    public enum Role { USER, AI }

    private final Role role;
    private final String text;

    public Message(Role role, String text) {
        this.role = role;
        this.text = text;
    }

    public Role getRole() {
        return role;
    }

    public String getText() {
        return text;
    }
}
