package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.CategoryDB;
import com.github.kickshare.domain.Category;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
@Mapper(config = CentralConfig.class)
public interface CategoryMapper {

    Category toDomain(CategoryDB source);

    List<Category> toDomain(List<CategoryDB> source);

    CategoryDB toDB(Category source);

    List<CategoryDB> toDB(List<Category> source);

}
