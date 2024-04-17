package SogangSolutionShare.BE.infra.oauth.config;

import SogangSolutionShare.BE.infra.oauth.kakao.client.KaKaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KaKaoApiClient kakaoApiClient() {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();
        return build.createClient(KaKaoApiClient.class);
    }
}
