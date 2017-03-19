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
@RestController("elasticInfoEndpoint")
public class ElasticInfoEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticInfoEndpoint.class);

    @RequestMapping("/elastic/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("No Elastic yet");
    }
}
