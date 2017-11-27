package com.github.kickshare.service.impl;

import java.util.List;

import com.github.kickshare.db.dao.TagRepository;
import com.github.kickshare.db.jooq.tables.daos.Tag_2CategoryDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.TagDB;
import com.github.kickshare.db.jooq.tables.pojos.Tag_2CategoryDB;
import com.github.kickshare.domain.Tag;
import com.github.kickshare.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 17.7.2017
 */
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final Tag_2CategoryDaoDB tag2CategoryDao;

    @Override
    public List<Tag> getTags(final int categoryId) {
        return tagRepository.getTags(categoryId);
    }

    @Override
    @Transactional
    public Tag createTag(final String name, final int categoryId) {
        Short id = tagRepository.createReturningKey(new TagDB(null, name));
        tag2CategoryDao.insert(new Tag_2CategoryDB(categoryId, id));
        return new Tag(id, name);
    }
}
