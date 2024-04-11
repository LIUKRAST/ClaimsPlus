package net.frozenblock.liukrast.claimsplus.api;

import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.Set;

public class TriMap<V, K, T> {
    private final HashMap<V,K> mapA = new HashMap<>();
    private final HashMap<V,T> mapB = new HashMap<>();

    public void put(V key, K valueA, T valueB) {
        mapA.put(key, valueA);
        mapB.put(key, valueB);
    }

    public K getA(V key) {
        return mapA.get(key);
    }

    public T getB(V key) {
        return mapB.get(key);
    }

    public void forEach(TriConsumer<V,K,T> consumer) {
        mapA.forEach((key, a) -> consumer.accept(key, a, getB(key)));
    }

    public Set<V> keySet() {
        return mapA.keySet();
    }
}
