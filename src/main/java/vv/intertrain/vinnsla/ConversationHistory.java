package vv.intertrain.vinnsla;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ConversationHistory {
    private final List<Message> messages = new ArrayList<>();
    public void addUserMessage(String text) {
        messages.add(new Message(Message.Role.USER, text));
    }

    public void addAIMessage(String text) {
        messages.add(new Message(Message.Role.AI, text));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getMessagesAsJsonStr() {
        Gson gson = new Gson();
        return gson.toJson(this.messages);
    }

    public void clear() {
        messages.clear();
    }
}
