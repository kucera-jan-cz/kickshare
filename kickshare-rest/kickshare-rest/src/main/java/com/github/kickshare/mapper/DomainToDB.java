package com.github.kickshare.mapper;

/**
 * @author Jan.Kucera
 * @since 8.11.2017
 */
public interface DomainToDB<D, E> {

    E toDomain(D source);

    D toDB(E source);

//    default List<E> toDomain(List<D> source) {
//        List<E> destination = new ArrayList<>();
//
//        for (D sourceObj : source) {
//            destination.add(toDomain(sourceObj));
//        }
//
//        return destination;
//    }
//
//    default List<D> toDB(List<E> source) {
//        List<D> destination = new ArrayList<>();
//
//        for (E sourceObj : source) {
//            destination.add(toDB(sourceObj));
//        }
//
//        return destination;
//    }

}
