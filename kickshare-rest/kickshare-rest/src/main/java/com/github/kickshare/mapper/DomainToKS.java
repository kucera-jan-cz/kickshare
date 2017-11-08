package com.github.kickshare.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan.Kucera
 * @since 8.11.2017
 */
public interface DomainToKS <K, D> {

    D toDomain(K source);

    K toKS(D source);

    default List<D> toDomain(List<K> source) {
        List<D> destination = new ArrayList<>();

        for (K sourceObj : source) {
            destination.add(toDomain(sourceObj));
        }

        return destination;
    }

    default List<K> toKS(List<D> source) {
        List<K> destination = new ArrayList<>();

        for (D sourceObj : source) {
            destination.add(toKS(sourceObj));
        }

        return destination;
    }

}