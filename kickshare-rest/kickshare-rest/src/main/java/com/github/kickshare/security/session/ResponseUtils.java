package com.github.kickshare.security.session;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.security.session.model.Error;
import com.github.kickshare.security.session.model.Response;
import org.springframework.http.MediaType;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public final class ResponseUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private ResponseUtils() {
    }

    public static void sendError(HttpServletResponse response, int status, String message, Exception exception) throws IOException {
        Error error = new Error("authError", exception.getMessage());
        sendResponse(response, status, new Response(status, message, error));
    }


    public static void sendResponse(HttpServletResponse response, int status, Object object) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.getType());
        response.setStatus(status);

        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(object));
        writer.flush();
        writer.close();
    }
}
