package com.medical.med.model;

public enum SexType {
    MALE(0),
    FEMALE(1);

    private final int value;

    SexType(int value) {  // ← добавьте конструктор
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
