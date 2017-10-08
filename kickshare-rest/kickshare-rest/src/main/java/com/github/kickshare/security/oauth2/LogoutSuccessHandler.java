package com.github.kickshare.security.oauth2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 8.10.2017
 */
@Component
@AllArgsConstructor
public class LogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    private static final String BEARER_AUTH_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "authorization";

    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(BEARER_AUTH_PREFIX)) {
            final OAuth2AccessToken oAuth2AccessToken = tokenStore
                    .readAccessToken(StringUtils.substringAfter(token, BEARER_AUTH_PREFIX));

            if (oAuth2AccessToken != null) {
                tokenStore.removeAccessToken(oAuth2AccessToken);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
