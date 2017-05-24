package com.github.kickshare.rest.advice;

import com.github.kickshare.kickstarter.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Jan.Kucera
 * @since 22.5.2017
 */
@RestControllerAdvice
public class RESTExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(RESTExceptionAdvice.class);

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void handleJsonException(final AuthenticationException exception) {
        LOGGER.warn(exception.getMessage(), exception);
    }
}
