package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 22.5.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leader {
    private Long backerId;
    private String email;
    private Long kickstarterId;
}
