package ru.yagodin.kafkaexample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.DisconnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yagodin.kafkaexample.model.TestModel;
import ru.yagodin.kafkaexample.model.User;

import java.util.Random;

@Service
public class KafkaMessageTestService {

    private KafkaTemplate<String, TestModel> template;
    private KafkaTemplate<String, User> templateForStreams;
    private ObjectMapper objectMapper;
    private User testUser;


    @Autowired
    public KafkaMessageTestService(KafkaTemplate<String, TestModel> template, KafkaTemplate<String, User> templateForStreams) {
        this.template = template;
        this.templateForStreams = templateForStreams;
        objectMapper = new ObjectMapper();
        testUser = new User();
        testUser.setName("Vasiliy");
        testUser.setSecondName("Petrov");
        testUser.setBalance(1589.00);
        testUser.setPhoneNumber(1L);
    }


    public void sendToTest(TestModel model) {
        template.send("test", model);
    }

    public void sendToSecond(TestModel model) {
        template.send("second", model);
    }

    public void sendToSrc() {
        testUser.setPhoneNumber(testUser.getPhoneNumber()+1);
        templateForStreams.send("src", testUser.getPhoneNumber().toString(), testUser);
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
//        sendToTest(new TestModel("Name1", new Random().nextInt()));
//        sendToSecond(new TestModel("Name2", new Random(10).nextInt()));
        sendToSrc();
    }

}
