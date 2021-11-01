package ru.yagodin.kafkaexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yagodin.kafkaexample.service.KafkaMessageTestService;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private KafkaMessageTestService service;

    @Override
    public void run(String... args) throws Exception {
        service.tester();
    }
}
