package com.github.kickshare.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import org.dozer.Mapper;
import org.dozer.MappingException;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
//@TODO make it static or proxy
@AllArgsConstructor
public class MapperUtils {
    private Mapper mapper;

    public <T> List<T> map(Collection<?> source, Class<T> destinationClass) {
        return map(source, null, destinationClass);
    }

    public <T> List<T> map(Collection<? extends Object> source, List<T> destination, Class<T> destinationClass) {
        if (destination == null)
            destination = new ArrayList<T>();

        for (Object sourceObj : source) {
            destination.add(map(sourceObj, destinationClass));
        }

        return destination;
    }

    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        return mapper.map(source, destinationClass);
    }
}
