package com.demo.api.model;

import static com.demo.api.model.Permission.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests Permission enum")
class PermissionTest {

    @DisplayName("Should calculate authorisation")
    @ParameterizedTest(name = "{index} => httpMethod={0}, roleEntity={1}, serverPath={2}")
    @ArgumentsSource(CustomArgumentProvider.class)
    void sum(RequestMethod httpMethod, Set<Permission> permissions, String serverPath, boolean result) {
        assertEquals(result, Permission.hasPermission(httpMethod, permissions, serverPath));
    }

    static class CustomArgumentProvider implements ArgumentsProvider {

        private static final String USER_ENDPOINT_PATH_PATTERN = "/users";
        private static final String USER_ENDPOINT_PATH_PATTERN_EXTENDED = "/users/9d55ab9d-d2e7-429c-b519-1cda3852a40f";
        private static final String NONUSER_ENDPOINT_PATH_PATTERN = "/tasks";
        private static final String EMPTY_ENDPOINT_PATH_PATTERN = "";
        private static final String NULL_ENDPOINT_PATH_PATTERN = null;
        private static final Set<Permission> DEFAULT_PERMISSION = new HashSet<>();
        private static final Set<Permission> VIEW_PERMISSION = new HashSet<>() {{
            add(LIST_USER);
        }};
        private static final Set<Permission> SUPER_USER_PERMISSION = new HashSet<>() {{
            add(CREATE_USER);
            add(UPDATE_USER);
            add(DELETE_USER);
            add(LIST_USER);
        }};

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

            return Stream.of(
                    // default Role permission against correct User endpoint path
                    Arguments.of(RequestMethod.DELETE, DEFAULT_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.GET, DEFAULT_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.POST, DEFAULT_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.PUT, DEFAULT_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    // view Role permission against correct User endpoint path
                    Arguments.of(RequestMethod.DELETE, VIEW_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.GET, VIEW_PERMISSION, USER_ENDPOINT_PATH_PATTERN, true),
                    Arguments.of(RequestMethod.POST, VIEW_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.PUT, VIEW_PERMISSION, USER_ENDPOINT_PATH_PATTERN, false),
                    // super Role permission against correct User endpoint path
                    Arguments.of(RequestMethod.DELETE, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN, true),
                    Arguments.of(RequestMethod.GET, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN, true),
                    Arguments.of(RequestMethod.POST, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN, true),
                    Arguments.of(RequestMethod.PUT, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN, true),
                    // super Role permission against extended User endpoint path
                    Arguments.of(RequestMethod.DELETE, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN_EXTENDED, true),
                    Arguments.of(RequestMethod.GET, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN_EXTENDED, true),
                    Arguments.of(RequestMethod.POST, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN_EXTENDED, true),
                    Arguments.of(RequestMethod.PUT, SUPER_USER_PERMISSION, USER_ENDPOINT_PATH_PATTERN_EXTENDED, true),
                    // super Role permission against non-User endpoint path
                    Arguments.of(RequestMethod.DELETE, SUPER_USER_PERMISSION, NONUSER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.GET, SUPER_USER_PERMISSION, NONUSER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.POST, SUPER_USER_PERMISSION, NONUSER_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.PUT, SUPER_USER_PERMISSION, NONUSER_ENDPOINT_PATH_PATTERN, false),
                    // super Role permission against null endpoint path
                    Arguments.of(RequestMethod.DELETE, SUPER_USER_PERMISSION, NULL_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.GET, SUPER_USER_PERMISSION, NULL_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.POST, SUPER_USER_PERMISSION, NULL_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.PUT, SUPER_USER_PERMISSION, NULL_ENDPOINT_PATH_PATTERN, false),
                    // super Role permission against empty endpoint path
                    Arguments.of(RequestMethod.DELETE, SUPER_USER_PERMISSION, EMPTY_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.GET, SUPER_USER_PERMISSION, EMPTY_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.POST, SUPER_USER_PERMISSION, EMPTY_ENDPOINT_PATH_PATTERN, false),
                    Arguments.of(RequestMethod.PUT, SUPER_USER_PERMISSION, EMPTY_ENDPOINT_PATH_PATTERN, false)
            );

        }
    }

}