package jeonginho.chatgptapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChatGPTResponse
 * OpenAI API 응답 Dto
 *
 * story
 * choice
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResponse {
    private String story;
    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }
}
