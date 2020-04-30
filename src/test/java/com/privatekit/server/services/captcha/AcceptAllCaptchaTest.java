package com.privatekit.server.services.captcha;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptAllCaptchaTest {

    @Test
    public void testVerify() {
        assertTrue(new AcceptAllCaptcha().verify("some-token"));
    }
}
