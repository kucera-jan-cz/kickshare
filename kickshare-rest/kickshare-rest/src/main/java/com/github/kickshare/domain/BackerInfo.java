package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 27.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackerInfo {
    private Backer backer;
    private Address address;
}
