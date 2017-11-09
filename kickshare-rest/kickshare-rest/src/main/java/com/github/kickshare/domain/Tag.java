package com.github.kickshare.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 17.7.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Short id;

    @NotNull
    @Size(min = 1, max = 32)
    private String name;
}
