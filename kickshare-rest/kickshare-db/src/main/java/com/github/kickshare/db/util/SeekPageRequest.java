package com.github.kickshare.db.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Jan.Kucera
 * @since 27.6.2017
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeekPageRequest implements Pageable, InitializingBean {
    private String id;
    @Deprecated
    //@deprecated - page is not use once id is present
    private int page;
    private int size;
    private PageRequest pageRequest;


    @Override
    public void afterPropertiesSet() {
        this.pageRequest = new PageRequest(page, size);
    }

    private PageRequest getRequest() {
        if (this.pageRequest == null) {
            this.pageRequest = new PageRequest(page, size);
        }
        return pageRequest;
    }

    @Override
    public Sort getSort() {
        return getRequest().getSort();
    }

    @Override
    public Pageable next() {
        return getRequest().next();
    }

    public PageRequest previous() {
        return getRequest().previous();
    }

    @Override
    public Pageable first() {
        return getRequest().first();
    }

    public boolean equals(final Object obj) {
        return getRequest().equals(obj);
    }

    public int hashCode() {
        return getRequest().hashCode();
    }

    public String toString() {
        return getRequest().toString();
    }

    @Override
    public int getPageSize() {
        return getRequest().getPageSize();
    }

    @Override
    public int getPageNumber() {
        return getRequest().getPageNumber();
    }

    @Override
    public int getOffset() {
        return getRequest().getOffset();
    }

    @Override
    public boolean hasPrevious() {
        return getRequest().hasPrevious();
    }

    @Override
    public Pageable previousOrFirst() {
        return getRequest().previousOrFirst();
    }

}
