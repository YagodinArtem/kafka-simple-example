package ru.yagodin.kafkaexample.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.yagodin.kafkaexample.model.User;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Log4j2
public class KafkaStreamsConfig {

    private static String INPUT_TOPIC = "src";
    private static String OUTPUT_TOPIC = "out";

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.AT_LEAST_ONCE);
        props.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, 5000);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "KafkaTestApplication");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public Serde<User> userSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(User.class));
    }

    @Bean
    public KStream<String, User> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, String> stream = kStreamBuilder
                .stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String())); //init kStream for INPUT_TOPIC

        KStream<String, User> userStream = stream
                .mapValues(this::getUserFromString); // do nothing and get all
        userStream.foreach((s, u) -> System.out.println(s + " " + u)); //do something with messages
        userStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), userSerde())); //reroute messages to other topic
        return userStream;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    User getUserFromString(String userString) {
        User user = null;
        try {
            user = objectMapper().readValue(userString, User.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }
}
