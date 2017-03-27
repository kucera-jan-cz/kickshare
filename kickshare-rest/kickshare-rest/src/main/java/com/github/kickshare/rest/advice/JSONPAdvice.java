package com.github.kickshare.rest.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * @author Jan.Kucera
 * @since 25.3.2017
 */
@ControllerAdvice
public class JSONPAdvice extends AbstractJsonpResponseBodyAdvice {
    public JSONPAdvice() {
        super("callback");
    }
}
