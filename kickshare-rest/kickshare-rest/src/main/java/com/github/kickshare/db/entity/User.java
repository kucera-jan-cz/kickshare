package com.github.kickshare.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 31.3.2017
 */
@Entity(name = "user")
@Data
public class User {
    @Id
    private Long id;
    private String email;
    private String name;
    private String surname;
}
