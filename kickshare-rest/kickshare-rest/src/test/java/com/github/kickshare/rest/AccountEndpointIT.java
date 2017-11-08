package com.github.kickshare.rest;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.github.kickshare.test.LazyIntegrationTest;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 2.10.2017
 */
public class AccountEndpointIT extends LazyIntegrationTest {
    private GreenMail smtpServer;


    @BeforeClass
    public void setUp() throws Exception {
        smtpServer = new GreenMail(new ServerSetup(25, null, "smtp"));
        smtpServer.start();
    }

    @AfterClass
    public void tearDown() throws Exception {
        smtpServer.stop();
    }

    @Test
    public void changePassword() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
    }
}
