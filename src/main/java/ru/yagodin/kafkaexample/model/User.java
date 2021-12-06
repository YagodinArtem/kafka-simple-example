package ru.yagodin.kafkaexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("name")
    private String name;
    @JsonProperty("second_name")
    private String secondName;
    @JsonProperty("phone_number")
    private Long phoneNumber;
    @JsonProperty("balance")
    private Double balance;
}
