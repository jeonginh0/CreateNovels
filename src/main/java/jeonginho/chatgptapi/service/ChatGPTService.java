package jeonginho.chatgptapi.service;

import java.util.List;

public interface ChatGPTService {
    String generateText(String prompt);
    List<String> generateChoices(String story);
    String generatePrompt(String background, String main, String sub1, String sub2, String setting);
    String generateNextPrompt(String prevStory, String choice);
}
