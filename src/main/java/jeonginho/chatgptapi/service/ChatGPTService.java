package jeonginho.chatgptapi.service;

import java.util.List;

public interface ChatGPTService {
    String generateText(String prompt);
    String prompt();
    String continuePrompt(String prevStory, String choice);
}
