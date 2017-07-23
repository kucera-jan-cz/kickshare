package com.github.kickshare.service;

import java.util.List;

import com.github.kickshare.db.dao.TagRepository;
import com.github.kickshare.db.jooq.tables.daos.Tag_2CategoryDao;
import com.github.kickshare.db.jooq.tables.pojos.Tag_2Category;
import com.github.kickshare.domain.Tag;
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
    private final Tag_2CategoryDao tag2CategoryDao;

    @Override
    public List<Tag> getTags(final int categoryId) {
        return tagRepository.getTags(categoryId);
    }

    @Override
    @Transactional
    public Tag createTag(final String name, final int categoryId) {
        Short id = tagRepository.createReturningKey(new com.github.kickshare.db.jooq.tables.pojos.Tag(null, name));
        tag2CategoryDao.insert(new Tag_2Category(categoryId, id));
        return new Tag(id, name);
    }
}
