package ru.yagodin.kafkaexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yagodin.kafkaexample.service.KafkaMessageTestService;

@SpringBootTest
class KafkaExampleApplicationTests {

    @Autowired
    private KafkaMessageTestService testService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() {
        testService.tester();
    }

}
