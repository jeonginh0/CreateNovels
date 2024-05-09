package jeonginho.chatgptapi.dto;

import lombok.Data;

/**
 * Choice
 * GPT가 제공한 선택지를 선택하기위한 Dto
 *
 * message
 * */

@Data
public class Choice {
    private Message message;
}
