package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.GROUP_POST;

import java.sql.Timestamp;

import com.github.kickshare.db.jooq.tables.daos.GroupPostDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupPostDB;
import com.github.kickshare.db.jooq.tables.records.GroupPostRecordDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Repository
public class GroupPostRepositoryImpl extends AbstractRepository<GroupPostRecordDB, GroupPostDB, Long> implements GroupPostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupPostRepositoryImpl.class);

    @Autowired
    public GroupPostRepositoryImpl(final GroupPostDaoDB dao) {
        super(dao);
    }

    @Override
    public void updatePost(final GroupPostDB entity) {
        this.dsl
                .update(GROUP_POST)
                .set(GROUP_POST.POST_TEXT, entity.getPostText())
                .set(GROUP_POST.POST_MODIFIED, new Timestamp(System.currentTimeMillis()))
                .set(GROUP_POST.POST_EDIT_COUNT, GROUP_POST.POST_EDIT_COUNT.plus(1))
                .where(GROUP_POST.POST_ID.eq(entity.getPostId()))
                .execute();
    }

}
