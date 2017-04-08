package com.github.kickshare.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Entity(name = "project")
@Data
@Deprecated
public class ProjectOld {
    @Id
    private Long id;
    private String name;
    private String description;
    private String url;
    private java.sql.Date deadline;
}
