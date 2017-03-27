package com.github.kickshare.kickstarter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.github.kickshare.kickstarter.exception.ParsingException;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public class UserServiceImpl implements UserService {
    public List<Object> getBackedProjects(String userId) throws IOException, ParsingException {
        return Collections.emptyList();
    }
}
