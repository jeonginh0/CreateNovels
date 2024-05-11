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

/**
 * ChatGPTServiceImpl
 * ChatGPTService 인터페이스의 구현체
 *
 * 세부 사항 메서드를 정리한 구현체
 * */

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
        /**
         * ChatGPTRequest 객체 생성.
         * model : 챗봇 모델의 상태를 나타내는 정보 파라미터
         * prompt : 챗봇에 입력될 문장이나 질문 파라미터
         * */
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        /**
         * ChatGPTResponse 객체 생성
         * apiURL : 요청을 보낼 API의 URL이 전달되는 파라미터
         * request : 요청 객체 파라미터
         * ChatGPTResponse.class : 요청을 보낸 후 받을 응답의 타입 클래스 파라미터
         * */
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
        choices.add("선택지 1 :");
        choices.add("선택지 2 :");
        choices.add("선택지 3 :");
        return choices;
    }

    @Override //초기 프롬프트를 생성하는 메서드
    public String firstPrompt(String background, String main, String sub1, String sub2, String setting) {
        return String.format("배경은 %s이고 등장인물은 주인공(%s)과 %s,%s이 나오는 %s장르의 소설을 만들어줘 대화도 있으면 좋겠어."
                + " 추가적으로 긴 내용의 소설을 부탁할게. "
                + "또한 끝에 사용자에게 스토리의 방향성을 제시하는 선택지(3개)도 넣어줘\n\n###\n\n", background, main, sub1, sub2, setting);
    }

    @Override //다음 스토리(프롬프트)를 생성하는 메서드
    public String nextPrompt(String prevStory, String choice) {
        return String.format("이전 스토리는 %s이고 이전 스토리에 대한 선택지로는 %s번을 선택할게. 선택한 선택지와 이전 스토리를 기반으로"
                + "소설을 이어서 작성해줘. 대신 자극적인 내용도 있어야 해. 또한 등장인물간 대화는 필수야."
                + "추가적으로 끝에 마찬가지로 사용자에게 스토리의 방향성을 제시하는 선택지(3개도) 꼭 넣어줘.\n\n###\n\n", prevStory, choice);
    }

    @Override //엔딩 스토리(프롬프트)를 생성하는 메서드
    public String finalPrompt(String prevStory) {
        return String.format("이전 스토리는 %s이고 이때까지의 스토리를 종합시킨다음 완결을 내줘. 그리고 이때까지 만든 소설을 다 합쳐서 보여줘." +
                "추가로 선택지는 더이상 필요없어.", prevStory);
    }
}
