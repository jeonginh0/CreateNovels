package jeonginho.chatgptapi.service;

import java.util.List;

public interface ChatGPTService {
    String generateText(String prompt);
    List<String> generateChoices(String story);
    String firstPrompt(String background, String main, String sub1, String sub2, String setting);
    String nextPrompt(String prevStory, String choice);
    String finalPrompt(String prevStory);
}
