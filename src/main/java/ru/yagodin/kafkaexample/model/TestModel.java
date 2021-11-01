package ru.yagodin.kafkaexample.model;

import java.io.Serializable;

public class TestModel implements Serializable {

    private String name;
    private Integer number;

    public TestModel() {
    }

    public TestModel(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
