package com.github.kickshare.mapper;

import static com.github.kickshare.mapper.EntityMapper.group;
import static com.github.kickshare.mapper.EntityMapper.project;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.db.jooq.tables.pojos.ProjectDB;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.Project;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 8.11.2017
 */
@Import(MappingConfiguration.class)
public class EntityMapperTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testProjectToDB() {
        final ProjectDB expected = EnhancedRandom.random(ProjectDB.class);
        Project project = project().toDomain(expected);
        final ProjectDB dbProject = project().toDB(project);
        assertReflectionEquals(expected, dbProject);
    }

    @Test
    public void testGroupToDB() {
        final List<GroupDB> expected = EnhancedRandom.randomListOf(2, GroupDB.class, "lat", "cityId", "lon", "isLocal");
        List<Group> domain = group().toDomain(expected);
        final List<GroupDB> dbEntity = group().toDB(domain);
        assertReflectionEquals(expected, dbEntity);
    }
}