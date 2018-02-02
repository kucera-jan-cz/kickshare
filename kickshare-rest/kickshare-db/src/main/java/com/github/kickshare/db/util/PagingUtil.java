package com.github.kickshare.db.util;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageRequest;

/**
 * @author Jan.Kucera
 * @since 12.12.2017
 */
public final class PagingUtil {
    private PagingUtil() {
    }

    public static <T> SelectForUpdateStep paginate(SelectConditionStep<Record> select, Field<T> field, SortOrder sort, SeekPageRequest page) {
        PageRequest limit = page.getPageRequest();
        SortField<T> sortField = field.sort(sort);
        if (StringUtils.isNotEmpty(page.getId())) {
            Param<T> param = DSL.val(page.getId(), field);
            return select.orderBy(sortField).seek(param).limit(limit.getPageSize());
        } else {
            return select.orderBy(sortField).limit(limit.getOffset(), limit.getPageSize());
        }
    }

    public static <T> Function<SelectConditionStep<Record>, SelectForUpdateStep<Record>> paginate(Field<T> field, SortOrder sort, SeekPageRequest page) {
        return (SelectConditionStep<Record> select) -> {
            PageRequest limit = page.getPageRequest();
            SortField<T> sortField = field.sort(sort);
            if (StringUtils.isNotEmpty(page.getId())) {
                Param<T> param = DSL.val(page.getId(), field);
                return select.orderBy(sortField).seek(param).limit(limit.getPageSize());
            } else {
                return select.orderBy(sortField).limit(limit.getOffset(), limit.getPageSize());
            }
        };
    }
}
