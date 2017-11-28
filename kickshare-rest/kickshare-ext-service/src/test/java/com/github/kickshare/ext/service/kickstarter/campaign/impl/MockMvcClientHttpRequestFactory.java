package com.github.kickshare.ext.service.kickstarter.campaign.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
public class MockMvcClientHttpRequestFactory implements ClientHttpRequestFactory {
    private ClientHttpRequest mockRequest;
    private ClientHttpResponse mockResponse;

    public MockMvcClientHttpRequestFactory(final InputStream stream) {
        this.mockRequest = mock(ClientHttpRequest.class);
        this.mockResponse = mock(ClientHttpResponse.class);
        try {
            when(mockRequest.execute()).thenReturn(mockResponse);
            when(mockResponse.getBody()).thenReturn(stream);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }


    @Override
    public ClientHttpRequest createRequest(final URI uri, final HttpMethod httpMethod) throws IOException {
        return mockRequest;
    }


}
