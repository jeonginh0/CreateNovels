package jeonginho.chatgptapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * ChatGPTConfig
 * 설정 정보
 *
 * openAiKey = your-secret-api-key
 * Authorizayion에 apikey값을 주입.
 * */

@Configuration
public class ChatGPTConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add("Authorization",
                    "Bearer "+openAiKey);
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
