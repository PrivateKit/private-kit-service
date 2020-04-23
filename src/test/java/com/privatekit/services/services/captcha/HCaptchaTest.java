package com.privatekit.services.services.captcha;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HCaptchaTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());

    private final String secret = "YOUR-SECRET";
    private final String token = "CLIENT-RESPONSE";
    private final String path = "/siteverify";

    @Test
    public void verifyTrue() {

        wireMockRule.stubFor(post(urlEqualTo(path))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalTo(String.format("response=%s&secret=%s", token, secret)))
                .willReturn(okJson(makeValidResponseBody(true))));

        verify(true);
    }

    @Test
    public void verifyFalse() {
        wireMockRule.stubFor(post(urlEqualTo(path)).willReturn(okJson(makeValidResponseBody(false))));
        verify(false);
    }

    @Test
    public void verifyFalseWhenInvalidStatusCode() {
        wireMockRule.stubFor(post(urlEqualTo(path)).willReturn(badRequest()));
        verify(false);
    }

    @Test
    public void verifyFalseWhenInvalidResponsePayload() {
        wireMockRule.stubFor(post(urlEqualTo(path)).willReturn(okJson("{}")));
        verify(false);
    }

    private void verify(Boolean expect) {
        assertEquals(new HCaptcha(secret, getHCaptchaUrl()).verify(token), expect);
    }

    private String getHCaptchaUrl() {
        return "http://localhost:" + wireMockRule.port() + path;
    }

    private String makeValidResponseBody(Boolean success) {
        return String.format("{\"success\": %s}", success);
    }
}
