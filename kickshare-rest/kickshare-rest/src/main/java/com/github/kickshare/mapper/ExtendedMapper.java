package com.github.kickshare.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import org.dozer.Mapper;
import org.dozer.MappingException;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
@AllArgsConstructor
public class ExtendedMapper implements Mapper {
    private final Mapper mapper;

    public <T> List<T> map(Collection<?> source, Class<T> destinationClass) {
        return map(source, null, destinationClass);
    }

    public <T> List<T> map(Collection<? extends Object> source, List<T> destination, Class<T> destinationClass) {
        if (destination == null) {
            destination = new ArrayList<>();
        }

        for (Object sourceObj : source) {
            destination.add(map(sourceObj, destinationClass));
        }

        return destination;
    }

    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        return mapper.map(source, destinationClass);
    }

    @Override
    public void map(final Object source, final Object destination) throws MappingException {
        mapper.map(source, destination);
    }

    @Override
    public <T> T map(final Object source, final Class<T> destinationClass, final String mapId) throws MappingException {
        return mapper.map(source, destinationClass, mapId);
    }

    @Override
    public void map(final Object source, final Object destination, final String mapId) throws MappingException {
        mapper.map(source, destination, mapId);
    }
}
