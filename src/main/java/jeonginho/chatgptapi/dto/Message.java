package jeonginho.chatgptapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Message
 * (TEST)
 *
 * role
 * content
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
