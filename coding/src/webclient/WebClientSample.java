/*

build.gradle에 추가

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-webflux'
}

*/

package webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class WebClientSample {

  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private final String RUN_URI = "/test/uri";
  
  @Value("${serverHost}")
  private String serverHost;

  @Value("${server.port}")
  private String port;

  @Value("${server.servlet.context-path}")
  private String context;
  
  
  
  public void test() {
    
    DTO dto = new DTO();
    dto.setId(0);
    Rule rule = new Rule(); 
    
    
    
    //실행 등록 : 비동기
    //http://localhost:4201/v1/test/uri
    Mono<Long> mono = WebClient.create(serverHost +":"+ port + context).post().uri(RUN_URI)
                              .contentType(MediaType.APPLICATION_JSON)
                              .accept(MediaType.APPLICATION_JSON)
                              .bodyValue(dto) // set body value
                              .retrieve() // client message 전송
                              .bodyToMono(Long.class)
                              ;
    
    mono.subscribe(result -> {
      logger.info("규칙ID:{}, 파일명:{}이 실행Id [{}]로 실행 되었습니다.",rule.getRuleId(), rule.getFileName(), result);
    });
    
    
    
    //실행 등록 : 동기
    Long rtn = WebClient.create(serverHost +":"+ port + context).post()
                        .uri(RUN_URI)
                        .bodyValue(dto) // set body value
                        .retrieve() // client message 전송
                        .bodyToMono(Long.class)
                        .block()
                        ;
    
    logger.info("규칙ID:{}, 파일명:{}이 실행Id [{}]로 실행 되었습니다.",rule.getRuleId(), rule.getFileName(), rtn);
  
  }
}
