package com.github.kickshare.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 3.10.2017
 */
public class BcryptTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcryptTest.class);

    @Test
    public void test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("newpassword");
        LOGGER.info("{}", encoded);
    }
}
