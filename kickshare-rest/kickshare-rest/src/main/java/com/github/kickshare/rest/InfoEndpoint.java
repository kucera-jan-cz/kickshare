package com.github.kickshare.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@RestController("systemInfoEndpoint")
public class InfoEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoEndpoint.class);

    @RequestMapping("/system/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("0.1-SNAPSHOT");
    }
}
