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

    @Override
    public String prompt(String background, String main, String sub1, String sub2, String setting) {
        return String.format("나는 너와 텍스트 게임을 해보려 해.\n" +
                "내가 인물, 배경, 장르를 제시하면 너는 내가 제시한 요소를 기반으로 소설을 만들어 주면 돼.\n" +
                "하지만 소설을 다 만들면 안돼. 중간 중간에 내가 선택해서 그에 따라 소설을 이어갈 수 있도록 잠시 끊고 선택지를 제공해줘.\n" +
                "예를 들어서 제일 처음에는 내가 [인물 : 남자아이, 배경 : 무림, 장르 : 무협]을 제공해주면" +
                "[무당파 본산 대문 앞, 어린 남자아이가 거지 차림으로 땅바닥 에 널부러져 있다. " +
                "이 아이는 산적에 의해 부모를 잃고 집 마저 잃게 되어 거지차림으로 이곳저곳 다니고 있었다. " +
                "돈이 한푼도 없던 이 아이는 지푸라기라도 잡는 심정으로 무당파 본산 대문 앞에서 이틀 째 자리잡고 있었다. " +
                "그러던 와중에 마침! 무당파 대문이 열렸다. 무당파 의복을 차려입은 사내들이 나왔다. 사내들이 남자아이를 보자마자 말을 건냈다. " +
                "“감히 무당파 대문 앞에서 뭐하는 짓거리냐?” 험악하게 말을 내뱉었다. " +
                "남자아이는 그 말이 들리지도 않는지 그저 무당파 사내들의 의복과 검을 보자마자 눈이 초롱초롱해져 무당파에 입문하고 싶다고 생각하고 있었다.] 처럼 소설을 만들어줘.\n" +
                "이에 더불어 [남자 아이는 어떻게 대답할까? 1: 무당파 입문 요청, 2: 동냥, 3: 지나가다 잠시 쉬어가는 중] 와 같이 선택지 3개 정도를 제공해줘.\n" +
                "이야기를 최대한 길게 만들고 싶으니까 최소한 5번은 선택지를 고를 수 있게 해주고 5번의 선택이 끝나면 이야기가 끝이나도록 해. \n" +
                "선택지는 선택지 번호(1,2,3)과 번호에 따른 내용을 제공해줘야 해.\n" +
                "- 조건을 정리해줄게.\n" +
                "1. 이 조건은 앞으로 쭉 이어진다.\n" +
                "2. 선택지는 번호와 함께 내용을 보여줘야 한다. \n" +
                "3. 처음에 선택지를 고를 때부터 카운트를 시작한다\n" +
                "4. 카운트가 5가 되면 이야기를 완결낸다.");
    }

    @Override //초기 프롬프트를 생성하는 메서드
    public String firstPrompt(String background, String main, String sub1, String sub2, String setting) {
        return String.format("배경은 %s이고 등장인물은 주인공(%s)과 %s,%s이 나오는 %s장르의 소설을 만들어줘 대화도 있으면 좋겠어."
                + " 추가적으로 긴 내용의 소설을 부탁할게. "
                + "또한 끝에 사용자에게 스토리의 방향성을 제시하는 선택지(3개)도 넣어줘. 첫 번째 선택지 문장의 첫 글자 앞에 $, 마지막 글자 뒤에 $를 넣고,"
                + " 두 번째 선택지 문장의 첫 글자 앞에 @ 마지막 글자 뒤에 @를 넣어줘. 그리고 세 번째 선택지 문장의 첫 글자 앞에 <, 마지막 글자 뒤에 >를 넣어줘. \n\n###\n\n", background, main, sub1, sub2, setting);
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
                "그리고 선택지는 더이상 필요없어.", prevStory);
    }
}
