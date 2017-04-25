package com.github.kickshare.db.dao;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.domain.City;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.service.GroupSearchOptions;
import com.github.kickshare.service.Location;
import com.github.kickshare.service.entity.CityGrid;
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
    ProjectInfo findProjectInfo(Long projectId);

    List<GroupInfo> findAllGroupInfo(Long projectId);


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

    List<CityGrid> searchCityGrid(GroupSearchOptions options) throws IOException;
}
