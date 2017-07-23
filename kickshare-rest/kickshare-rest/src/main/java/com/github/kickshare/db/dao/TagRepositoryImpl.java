package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.TAG;
import static com.github.kickshare.db.jooq.Tables.TAG_2_CATEGORY;

import java.util.List;

import com.github.kickshare.db.jooq.tables.daos.TagDao;
import com.github.kickshare.db.jooq.tables.pojos.Tag;
import com.github.kickshare.db.jooq.tables.records.TagRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 18.7.2017
 */
@Repository
public class TagRepositoryImpl extends AbstractRepository<TagRecord, Tag, Short> implements TagRepository {

    @Autowired
    public TagRepositoryImpl(TagDao dao) {
        super(dao);
    }

    @Override
    public List<com.github.kickshare.domain.Tag> getTags(final int categoryId) {
        return dsl.select()
                .from(TAG)
                .join(TAG_2_CATEGORY).on(TAG.ID.eq(TAG_2_CATEGORY.TAG_ID))
                .where(TAG_2_CATEGORY.CATEGORY_ID.eq(categoryId))
                .fetchInto(com.github.kickshare.domain.Tag.class);
    }
}
