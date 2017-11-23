package com.github.kickshare.service.util;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Collections;

import com.github.kickshare.common.io.Lists;
import com.github.kickshare.domain.GroupSummary;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 23.11.2017
 */
public class GroupNameFactoryTest {
    private GroupNameFactory builder = new GroupNameFactory();

    @Test
    public void globalGroups() {
        String name = builder.nextName("Quodd Heroes", "CZ", Lists.of(
                group("Quodd Heroes - CZ"),
                group("Quodd Heroes - Brno"),
                group("Quodd Heroes - Praha")
                )
        );
        assertEquals("Quodd Heroes - CZ #01", name);
    }

    @Test
    public void noGroups() {
        String name = builder.nextName("Quodd Heroes", "CZ", Collections.emptyList());
        assertEquals("Quodd Heroes - CZ", name);
    }

    @Test
    public void localGroups() {
        String name = builder.nextName("Quodd Heroes", "Brno", Lists.of(
                group("Quodd Heroes - CZ"),
                group("Quodd Heroes - Brno"),
                group("Quodd Heroes - Praha")
        ));
        assertEquals("Quodd Heroes - Brno #01", name);
    }

    @Test
    public void newLocalGroups() {
        String name = builder.nextName("Quodd Heroes", "Brno", Lists.of(
                group("Quodd Heroes - CZ"),
                group("Quodd Heroes - Brno"),
                group("Quodd Heroes - Brno #01"),
                group("Quodd Heroes - Praha")
        ));
        assertEquals("Quodd Heroes - Brno #02", name);
    }

    @Test
    public void newGlobalGroups() {
        String name = builder.nextName("Quodd Heroes", "Praha", Lists.of(
                group("Quodd Heroes - CZ"),
                group("Quodd Heroes - Brno")
        ));
        assertEquals("Quodd Heroes - Praha", name);
    }

    private GroupSummary group(String name) {
        GroupSummary groupSummary = new GroupSummary();
        groupSummary.setName(name);
        return groupSummary;
    }
}