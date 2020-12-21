package com.demo.api.filter;

import com.demo.api.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class BaseAuthFilter extends OncePerRequestFilter {

    protected static final List<String> EXCLUDE_URL = Arrays.asList("/auth/login", "/auth/forgot");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  throws ServletException, IOException {
        if (!HttpServletRequest.class.isInstance(request)) {
            response.sendError(HttpStatus.BAD_REQUEST.value());
            return;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return AuthUtil.verifyPathPattern(EXCLUDE_URL, getPathWithinApplication(request));
    }

    protected String getPathWithinApplication(HttpServletRequest request) {
        return new UrlPathHelper().getPathWithinApplication(request);
    }

}
