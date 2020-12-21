package com.demo.api.filter;

import com.demo.api.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends BaseAuthFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        super.doFilterInternal(request, response, chain);

        if (AuthUtil.isAuthenticationRequired(request)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Not authenticated");
            return;
        }

        chain.doFilter(request, response);
    }

}
