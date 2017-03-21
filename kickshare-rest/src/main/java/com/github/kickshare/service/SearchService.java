package com.github.kickshare.service;

import java.util.List;
import java.util.Objects;

import com.github.kickshare.service.entity.City;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public interface SearchService {
    public List<Object> searchGroups();

    public Object getUserLocation(String userId);

    public Object getGroupById(String groupId);

    public City getCityById(String id);

}
