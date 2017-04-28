package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 27.4.2017
 */
@Data
@AllArgsConstructor
public class BackerInfo {
    private User backer;
    private Address address;
}
