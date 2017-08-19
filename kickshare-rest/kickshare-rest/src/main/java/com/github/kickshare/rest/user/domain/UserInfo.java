package com.github.kickshare.rest.user.domain;

import javax.validation.constraints.Size;

import com.github.kickshare.domain.Address;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Email;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
public class UserInfo {
    @Email
    @NonNull
    private String email;

    @NonNull
    private String name;
    @NonNull
    private String surname;

    @NonNull
    @Size(min = 8)
    private String password;

    @NonNull
    private Address address;

}
