package com.github.kickshare.service;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.service.entity.CityGrid;
import com.github.kickshare.service.entity.Group;
import com.github.kickshare.service.entity.SearchOptions;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public interface SearchService {
    List<Group> searchGroups(SearchOptions options) throws IOException;

    List<CityGrid> searchCityGrid(SearchOptions options) throws IOException;
}
