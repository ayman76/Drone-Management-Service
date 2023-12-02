package com.example.technicaltask.model;

import lombok.Getter;

@Getter
public enum Model {
    Lightweight(100),
    Middleweight(200),
    Cruiserweight(300),
    Heavyweight(500);

    private final int value;

    Model(int value) {
        this.value = value;
    }

}