package com.github.kickshare.rest.group;

import java.util.Collections;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@RestController
@RequestMapping("/groups")
public class GroupEndpoint {
    //Search for groups using user's location, using distance near (slider), tags, potentially campaign's name

    @PostMapping("/search")
    public List<Object> searchGroups(String userId, String categoryId, SearchGroupOptions options) {
        return Collections.emptyList();
    }

}
