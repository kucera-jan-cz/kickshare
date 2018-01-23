package com.github.kickshare.db.dao;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.db.pojos.CityGridDB;
import com.github.kickshare.db.query.LocationDB;
import com.github.kickshare.db.query.SearchOptionsDB;
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

    List<CityDB> findCitiesWithing(LocationDB ne, LocationDB sw);

    List<GroupDB> searchGroups(SearchOptionsDB options);

    List<CityGridDB> searchCityGrid(SearchOptionsDB options) throws IOException;

}
