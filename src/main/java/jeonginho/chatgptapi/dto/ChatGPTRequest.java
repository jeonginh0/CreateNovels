package jeonginho.chatgptapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatGPTRequest
 * OpenAI API 요청 Dto
 *
 * model
 * message
 * */
@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
