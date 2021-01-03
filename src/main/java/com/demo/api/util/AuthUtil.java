package com.demo.api.util;

import org.springframework.util.AntPathMatcher;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.function.IntUnaryOperator;

public final class AuthUtil {

    private static final int ADMIN_INIT_PASSWORD_SIZE = 20;
    private static final int FORGOT_PASSWORD_TOKEN_SIZE = 6;
    private static final int[] SPECIAL_CHARACTER_CODES = {34, 39, 47};
    private static final byte[] SALT = "Lorem ipsum".getBytes();
    public static final String AUTHENTICATION_TOKEN_ATTR = "token";
    public static final String AUTHENTICATED_USER_ATTR = "userId";

    private AuthUtil() {
        // no action required
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName) {
        Object attribute = request.getSession().getAttribute(attributeName);
        return attribute == null ? null : (T) attribute;
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
        return generateString(ADMIN_INIT_PASSWORD_SIZE, 33, 122);
    }

    public static String generateToken() {
        return generateString(FORGOT_PASSWORD_TOKEN_SIZE, 48, 57);
    }

    private static String generateString(int size, int asciiFrom, int asciiTo) {
        IntUnaryOperator exclusionOperator = (i) -> Arrays.asList(SPECIAL_CHARACTER_CODES).contains(i) ? i++ : i;
        return new Random().ints(size, asciiFrom, asciiTo)
                .map(exclusionOperator)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String hash(String password) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 128);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                    .generateSecret(spec)
                    .getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
