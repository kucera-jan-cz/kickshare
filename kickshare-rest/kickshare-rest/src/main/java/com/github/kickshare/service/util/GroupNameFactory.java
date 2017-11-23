package com.github.kickshare.service.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.kickshare.domain.GroupSummary;

/**
 * @author Jan.Kucera
 * @since 23.11.2017
 */
public class GroupNameFactory {
    private static final Pattern NAME_REGEX = Pattern.compile(".+ - (\\w+) (#(\\d+))$");
    private static final Integer DEFAULT_GROUP_ID = 1;
    private static final Integer NO_GROUP_ID = -1;

    public String nextName(String projectName, String location, List<GroupSummary> existingGroups) {
        String template = MessageFormat.format("{0} - {1}", projectName, location);
        List<Integer> ids = existingGroups.stream()
                .map(GroupSummary::getName)
                .filter(n -> n.startsWith(template))
                .map(this::parseNumber)
                .collect(Collectors.toList());

        Integer id = existingGroups.stream()
                .map(GroupSummary::getName)
                .filter(n -> n.startsWith(template))
                .map(this::parseNumber)
                .reduce(Integer::max)
                .orElse(NO_GROUP_ID);
        if(id < DEFAULT_GROUP_ID) {
            return MessageFormat.format("{0} - {1}", projectName, location);
        } else {
            return MessageFormat.format("{0} - {1} #{2,number,00}", projectName, location, ++id);
        }
    }


    private Integer parseNumber(String groupName) {
        Matcher matcher = NAME_REGEX.matcher(groupName);
        if(matcher.matches()) {
            return Integer.valueOf(matcher.group(3));
        } else {
            return DEFAULT_GROUP_ID;
        }
    }
}
