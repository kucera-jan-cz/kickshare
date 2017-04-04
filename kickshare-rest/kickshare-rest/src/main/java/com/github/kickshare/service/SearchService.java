package com.github.kickshare.service;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.service.entity.City;
import com.github.kickshare.service.entity.Group;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public interface SearchService {
    public List<Group> searchCities(GroupSearchOptions options) throws IOException;

    public List<Object> searchGroups();

    public Object getUserLocation(String userId);

    public Object getGroupById(String groupId);

    public City getCityById(String id);

}
