package com.privatekit.server.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CaptchaFilterPathMatcherElement {

    public static CaptchaFilterPathMatcherElement from(String pattern, String... methods) {
        return new CaptchaFilterPathMatcherElement(pattern, new HashSet<>(Arrays.asList(methods)));
    }

    private final String pattern;
    private final Set<String> methods;

    public CaptchaFilterPathMatcherElement(String pattern, Set<String> methods) {
        this.pattern = pattern;
        this.methods = methods;
    }

    public boolean match(String path, String method) {
        return new AntPathMatcher().match(pattern, sanitizePath(path)) && methods.contains(method);
    }

    private String sanitizePath(String path) {
        return StringUtils.trimTrailingCharacter(path, '/');
    }
}
