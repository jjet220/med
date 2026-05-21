package com.medical.med.model.enums;

public enum DocumentType {
    PASSPORT(1),
    CERTIFICATE_OF_BIRTH(2);

    private final int value;

    DocumentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
