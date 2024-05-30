package jeonginho.chatgptapi.service.implement;

import jeonginho.chatgptapi.dto.ChatGPTRequest;
import jeonginho.chatgptapi.dto.ChatGPTResponse;
import jeonginho.chatgptapi.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Override // 추가
    public String prompt() {
        return String.format("#명령문\n" +
                "나는 텍스트 게임을 너와 해보려고 해.\n" +
                "내가 인물, 배경, 장르를 제시하면, 너는 내가 제시한 요소를 기반으로 소설을 만들어줘. 대신에, 소설을 다 만들면 안 돼. 중간 중간에 내가 선택해서 그에 따라 소설을 이어갈 수 있도록 끊고 선택지를 제공해줘.\n" +
                "\n" +
                "#예시\n" +
                "입력문: 인물1: 리자(이름), ENTP(MBTI), 자작의 예쁨을 많이 받은 자작 집안의 막내 딸, 인물2: 더글라스(이름), ISTJ(MBTI), 괴물이라는 무서운 소문이 있어 차가워 보이지만, 알고 보면 자상한 북부 공작, 인물3: 알버트(이름), ESFJ(MBTI), 리자의 소꿉친구로 남작집의 차기 남작 후보로, 강아지 같은 매력이 있음, 시대: 중세시대, 장르: 로맨스 판타지\n" +
                "출력문: 중세시대 무렵, 어느 한 자작집의 예쁨 받는 막내 딸인 리자는 혼기가 차서 약혼할 상대를 찾고 있었다. 그러던 중, 파티가 열린다고 하여 북부 공작 더글라스와 소꿉친구 알버트 둘 중 한 사람에게 같이 파트너를 부탁해볼까 고민 중이다.\n" +
                "\n" +
                "리자는 누구에게 초대장을 보낼까?\n" +
                "1: 북부 공작 더글라스\n" +
                "2: 소꿉친구 알버트\n" +
                "\n" +
                "#제약조건\n" +
                "선택지를 골라 이야기를 만드는 과정이 10번이 되면, 이야기가 마무리되어야 해.\n" +
                "인물 간의 대화가 많았으면 해.\n" +
                "또한 내용이 길어야해\n" +
                "\n" +
                "#입력문\n" +
                "인물1: 홍설(이름), ESTP(MBTI), 조선시대 양반집의 딸로 태어났지만 집안이 역모죄 때문에 망해서 남장을 하며 광대로 살아가고 있음, 인물2: 김우빈(이름), ENFJ(MBTI), 조선시대 양반집의 잘생기고 모두가 좋아하는 남자, 인물3: 이율(이름), ISTP(MBTI), 조선시대 츤데레 면모가 있는 세자, 김우빈과 이율은 어렸을 때는 친한 친구였지만, 김우빈의 집안이 현대 왕을 몰아내려 하기에 지금은 서먹한 사이, 김우빈과 홍설은 저작거리에서 만나 친해진 사이, 홍설과 이율은 어렸을 때 잠깐 만났지만 지금은 서로 못 알아보는 상태, 배경: 조선시대, 장르: 로맨스\n" +
                "\n" +
                "*위 내용을 1. 줄거리, 2. 대화, 3.선택지 로 구분해서 출력해줘." +
                "json 형식으로 부탁해. 또한 각 변수들은 줄거리는 story, 대화는 dialog, 화자는 speaker, 대사는 content, 선택지는 choice 1, 2, 3등으로 부탁할게.");
    }

    @Override // 추가
    public String continuePrompt(String prevStory, String choice) {
        return String.format("이전 스토리는 %s이고 이전 스토리에 대한 선택지로는 %s번을 선택할게. 선택한 선택지와 이전 스토리를 기반으로"
                + "소설을 이어서 작성해줘. 대신 자극적인 내용도 있어야 해. 또한 등장인물간 대화는 필수야."
                + "추가적으로 끝에 마찬가지로 사용자에게 스토리의 방향성을 제시하는 선택지(3개도) 꼭 넣어줘. \n" +
                "- 조건을 정리해줄게.\n" +
                "1. 선택지는 번호와 함께 내용을 보여줘야 한다. \n", prevStory, choice);
    }
}
