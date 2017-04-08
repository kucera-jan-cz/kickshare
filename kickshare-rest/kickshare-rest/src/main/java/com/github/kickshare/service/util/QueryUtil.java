package com.github.kickshare.service.util;

import java.io.IOException;
import java.text.MessageFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.common.io.ResourceUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author Jan.Kucera
 * @since 5.4.2017
 */
public class QueryUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static QueryBuilder toQuery(String path, Object... params) throws IOException {
        String templateAsText = ResourceUtil.toString(path);
        String[] paramsAsJson = new String[params.length];
        for(int i = 0; i < params.length; i++) {
            paramsAsJson[i] = mapper.writeValueAsString(params[i]);
        }
        return QueryBuilders.wrapperQuery(MessageFormat.format(templateAsText, paramsAsJson));
    }
}
