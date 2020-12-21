package com.demo.api.util;

import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class AuthUtil {

    private static final int ADMIN_INIT_PASSWORD_SIZE = 20;
    private static final int FORGOT_PASSWORD_TOKEN_SIZE = 6;
    public static final String AUTHENTICATION_TOKEN_ATTR = "token";
    public static final String AUTHENTICATED_USER_ATTR = "userId";

    private AuthUtil() {
        // no action required
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName) {
        Object token = request.getSession().getAttribute(attributeName);
        return token == null ? null : (T) token;
    }

    public static boolean isAuthenticationRequired(HttpServletRequest request) {
        return AuthUtil.<UUID>getSessionAttribute(request, AUTHENTICATED_USER_ATTR) == null;
    }

    public static boolean verifyPathPattern(List<String> patternList, String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        return patternList.stream()
                .anyMatch(p -> matcher.match(p, path));
    }

    public static String generatePassword() {
        return generateString(ADMIN_INIT_PASSWORD_SIZE, 35, 122);
    }

    public static String generateToken() {
        return generateString(FORGOT_PASSWORD_TOKEN_SIZE, 48, 57);
    }

    private static String generateString(int size, int asciiFrom, int asciiTo) {
        return new Random().ints(size, asciiFrom, asciiTo)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
