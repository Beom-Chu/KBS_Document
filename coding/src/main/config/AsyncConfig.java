package main.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);  //기본적으로 실행을 대기하고 있는 Thread의 갯수
    executor.setMaxPoolSize(10);  //동시 동작하는, 최대 Thread 갯수
    executor.setQueueCapacity(500); //MaxPoolSize를 초과하는 요청시 저장 공간
    executor.setThreadNamePrefix("test-async-");  //spring이 생성하는 쓰레드의 접두사
    executor.initialize();
    return executor;
  }
}
