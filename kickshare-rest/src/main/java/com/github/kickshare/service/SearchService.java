package com.github.kickshare.service;

import java.util.List;
import java.util.Objects;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public interface SearchService {
    public List<Object> searchGroups();

    public Object getUserLocation(String userId);

    public Object getGroupById(String groupId);


}
