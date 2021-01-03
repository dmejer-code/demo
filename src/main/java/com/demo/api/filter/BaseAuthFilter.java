package com.demo.api.filter;

import com.demo.api.util.AuthUtil;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public abstract class BaseAuthFilter extends OncePerRequestFilter {

    protected static final List<String> EXCLUDE_URL = Arrays.asList("/auth/login", "/auth/forgot");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return AuthUtil.verifyPathPattern(EXCLUDE_URL, getPathWithinApplication(request));
    }

    protected String getPathWithinApplication(HttpServletRequest request) {
        return new UrlPathHelper().getPathWithinApplication(request);
    }

}
