package org.diagnostic.domain.entities;

import lombok.Getter;

@Getter
public enum TomographyCategory {
    Stone("Piedra"),
    Normal("Normal"),
    Tumor("Tumor"),
    Cyst("Quiste"),
    STATELESS("Sin_estado"),
    Other("Otro");

    private String value;

    TomographyCategory(String value) {
        this.value = value;
    }
}