package org.tptacs.infraestructure.repositories;

import lombok.Getter;

import java.util.Objects;

@Getter
public class KvsItem {
    private String key;
    private Object value;

    public KvsItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvsItem kvsItem = (KvsItem) o;
        return Objects.equals(key, kvsItem.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    private KvsItem() {}

}
