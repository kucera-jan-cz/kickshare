package com.github.kickshare.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Entity(name = "GROUP")
@Data
public class Group {
    @Id
    private Long id;

    @Column(name = "leader_id")
    private Long leaderId;

    @Column(name = "project_id")
    private Long projectId;

    private String name;
}
