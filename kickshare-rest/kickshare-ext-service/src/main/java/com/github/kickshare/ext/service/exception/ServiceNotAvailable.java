package com.github.kickshare.ext.service.exception;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class ServiceNotAvailable extends RuntimeException {
    public ServiceNotAvailable(final String message) {
        super(message);
    }

    public ServiceNotAvailable(final String message, final Throwable cause) {
        super(message, cause);
    }
}
