package ru.yagodin.kafkaexample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yagodin.kafkaexample.model.TestModel;

import java.util.Random;

@Service
public class KafkaMessageTestService {

    private KafkaTemplate<String, TestModel> template;
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaMessageTestService(KafkaTemplate<String, TestModel> template) {
        this.template = template;
        objectMapper = new ObjectMapper();
    }


    public void sendToTest(TestModel model) {
        template.send("test", model);
    }

    public void sendToSecond(TestModel model) {
        template.send("second", model);
    }

    @KafkaListener(id = "test", topics = {"test", "second"}, containerFactory = "singleFactory")
    public void consume(TestModel model, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        switch (topic)
        {
            case "test" -> System.out.println("obtained from test: " + model);
            case "second" -> System.out.println("obtained from second: " + model);
        }
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    public void tester() {
        sendToTest(new TestModel("Name1", new Random().nextInt()));
        sendToSecond(new TestModel("Name2", new Random(10).nextInt()));
    }

}
