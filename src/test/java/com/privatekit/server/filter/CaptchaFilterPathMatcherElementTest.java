package com.privatekit.server.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CaptchaFilterPathMatcherElementTest {

    CaptchaFilterPathMatcherElement element;

    @BeforeEach
    void setUp() {
        element = CaptchaFilterPathMatcherElement.from("/v1.0/*/survey", "POST", "GET");
    }

    @Test
    public void testMatchTrue() {
        assertTrue(element.match("/v1.0/1/survey", "POST"));
        assertTrue(element.match("/v1.0/1/survey", "GET"));
    }

    @Test
    public void testMatchFalseForInvalidPath() {
        assertFalse(element.match("invalid", "POST"));
    }

    @Test
    public void testMatchFalseForInvalidMethod() {
        assertFalse(element.match("/v1.0/1/survey", "invalid"));
    }

    @Test
    public void testMatchTrueForTrailingSlash() {
        assertTrue(element.match("/v1.0/1/survey/", "POST"));
    }
}
