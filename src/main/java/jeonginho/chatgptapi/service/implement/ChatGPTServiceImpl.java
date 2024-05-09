package jeonginho.chatgptapi.service.implement;

import jeonginho.chatgptapi.dto.ChatGPTRequest;
import jeonginho.chatgptapi.dto.ChatGPTResponse;
import jeonginho.chatgptapi.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    @Override // API 호출하여 텍스트로 반환.
    public String generateText(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        // 생성된 텍스트를 반환.
        return response
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }

    @Override // 선택지를 생성하는 메서드
    public List<String> generateChoices(String story) {
        List<String> choices = new ArrayList<>();

        // 선택지 생성 로직 구현
        choices.add("Choice 1");
        choices.add("Choice 2");
        choices.add("Choice 3");
        return choices;
    }

    @Override //초기 프롬프트를 생성하는 메서드
    public String generatePrompt(String background, String main, String sub1, String sub2, String setting) {
        return String.format("배경은 %s이고 등장인물은 주인공(%s)과 %s,%s이 나오는 %s장르의 소설을 만들어줘 대화도 있으면 좋겠어."
                + " 추가적으로 긴 내용의 소설을 부탁할게. "
                + "또한 끝에 사용자에게 스토리의 방향성을 제시하는 선택지(3개)도 넣어줘\n\n###\n\n", background, main, sub1, sub2, setting);
    }

    @Override //초기 프롬프트를 생성하는 메서드
    public String generateNextPrompt(String prevStory, String choice) {
        return String.format("이전 스토리는 %s이고 이전 스토리에 대한 선택지로는 %s를 선택할게 선택한 선택지와 이전 스토리를 기반으로"
                + "소설을 이어서 작성해줘."
                + "추가적으로 끝에 마찬가지로 사용자에게 스토리의 방향성을 제시하는 선택지(3개도) 꼭 넣어줘.\n\n###\n\n", prevStory, choice);
    }
}
