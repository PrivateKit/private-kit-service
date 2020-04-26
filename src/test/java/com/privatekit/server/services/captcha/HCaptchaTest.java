package com.privatekit.server.services.captcha;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HCaptchaTest {

    private final String secret = "YOUR-SECRET";
    private final String token = "CLIENT-RESPONSE";
    private final String path = "/siteverify";

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void testVerifyTrue() {

        wireMockServer.stubFor(post(urlEqualTo(path))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalTo(String.format("response=%s&secret=%s", token, secret)))
                .willReturn(okJson(makeValidResponseBody(true))));

        verify(true);
    }

    @Test
    public void testVerifyFalse() {
        wireMockServer.stubFor(post(urlEqualTo(path)).willReturn(okJson(makeValidResponseBody(false))));
        verify(false);
    }

    @Test
    public void testVerifyFalseWhenInvalidStatusCode() {
        wireMockServer.stubFor(post(urlEqualTo(path)).willReturn(badRequest()));
        verify(false);
    }

    @Test
    public void testVerifyFalseWhenInvalidResponsePayload() {
        wireMockServer.stubFor(post(urlEqualTo(path)).willReturn(okJson("{}")));
        verify(false);
    }

    private void verify(Boolean expect) {
        assertEquals(new HCaptcha(secret, getVerifyUrl()).verify(token), expect);
    }

    private String getVerifyUrl() {
        return "http://localhost:" + wireMockServer.port() + path;
    }

    private String makeValidResponseBody(Boolean success) {
        return String.format("{\"success\": %s}", success);
    }
}
