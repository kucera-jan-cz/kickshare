package com.github.kickshare.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Jan.Kucera
 * @since 12.4.2017
 */
public class SchemaHttpFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaHttpFilter.class);
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
//        String schema = request.getHeader("country");
        String schema = "CZ";
        //@TODO - validate that schema is setup
        SchemaContextHolder.setSchema(schema);
        filterChain.doFilter(request, response);
    }
}
