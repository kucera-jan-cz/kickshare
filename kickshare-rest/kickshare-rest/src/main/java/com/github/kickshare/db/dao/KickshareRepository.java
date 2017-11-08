package com.github.kickshare.db.dao;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.service.entity.CityGrid;
import com.github.kickshare.service.entity.Location;
import com.github.kickshare.service.entity.SearchOptions;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 * @deprecated - this repo works as temporary repository until we will figure out how to split repositories
 */

@Deprecated
@Transactional
public interface KickshareRepository {

    //CAMPAIGN VIEW
    /** MAPA - To elastic
     *  REQ: project_id, location
     *  RESP: geo, local/global
     */

    /** HEADER
     * REQ: project_id
     * RESP: name, deadline, photo_url, url, [tags]
     */

    void saveProjects(final List<ProjectInfo> projects);

    //DASHBOARD
    /** COMBOBOX CATEGORY
     *  REQ: {}
     *  RESP: category_name, category_id
     */

    /** SEARCH OPTIONS
     *  REQ: name, is_local, category, [label], geo [sw,ne]
     *  RESP: geo, local/global, num_of_groups
     */

    /**
     * BODY with groupped campaigns
     *  REQ: name, is_local, category, [label], geo [sw,ne]
     *  RESP: Name, photo_url, url, project_id
     */

    List<City> findCitiesWithing(Location ne, Location sw);

    List<GroupDB> searchGroups(SearchOptions options);

    List<CityGrid> searchCityGrid(SearchOptions options) throws IOException;

}
