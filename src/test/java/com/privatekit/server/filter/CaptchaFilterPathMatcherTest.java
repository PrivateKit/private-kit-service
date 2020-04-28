package com.privatekit.server.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CaptchaFilterPathMatcherTest {

    private CaptchaFilterPathMatcher pathMatcher;

    @BeforeEach
    public void setup() {
        pathMatcher = CaptchaFilterPathMatcher.with(
                CaptchaFilterPathMatcherElement.from("/v1.0/*/survey", "POST"),
                CaptchaFilterPathMatcherElement.from("/v1.0/*/survey/*/reply", "GET")
        );
    }

    @Test
    public void testTrue() {
        assertTrue(pathMatcher.match("/v1.0/1/survey", "POST"));
        assertTrue(pathMatcher.match("/v1.0/1/survey/1/reply", "GET"));
    }

    @Test
    public void testFalse() {
        assertFalse(pathMatcher.match("invalid", "POST"));
        assertFalse(pathMatcher.match("/v1.0/1/survey/1/reply", "invalid"));
    }
}
