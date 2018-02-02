package com.github.kickshare.rest.resolver;

import com.github.kickshare.db.util.SeekPageRequest;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Jan.Kucera
 * @since 12.12.2017
 */
public class SeekPageResolver extends PageableHandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(SeekPageRequest.class);
    }

    @Override
    public SeekPageRequest resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) {
        PageRequest page = (PageRequest) super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        String idAsText = webRequest.getParameter("seek");
        return new SeekPageRequest(idAsText, 0, 0, page);
    }
}
