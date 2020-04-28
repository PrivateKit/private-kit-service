package com.privatekit.server.filter;

public class CaptchaFilterPathMatcher {

    public static CaptchaFilterPathMatcher with(CaptchaFilterPathMatcherElement... elements) {
        return new CaptchaFilterPathMatcher(elements);
    }

    private final CaptchaFilterPathMatcherElement[] elements;

    public CaptchaFilterPathMatcher(CaptchaFilterPathMatcherElement[] elements) {
        this.elements = elements;
    }

    public boolean match(String path, String method) {
        for (CaptchaFilterPathMatcherElement i : elements) {
            if (i.match(path, method)) {
                return true;
            }
        }
        return false;
    }
}
