package com.github.kickshare.domain;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
@AllArgsConstructor
public class Address {
    private Long id;
    private Long backerId;
    @NonNull
    @Size(min = 3)
    private String street;
    @NonNull
    @Size(min = 2)
    private String city;
    @NonNull
    private Integer cityId;

    @NonNull
    @Size(min = 4)
    private String postalCode;

    public Address() {
    }
}
