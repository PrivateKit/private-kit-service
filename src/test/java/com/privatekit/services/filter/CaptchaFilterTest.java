package com.privatekit.services.filter;

import com.privatekit.services.services.captcha.Captcha;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CaptchaFilterTest {

    @Test
    public void testRejectionForNotHavingAuthorizationHeader() throws Exception {
        testFilter(null, false, true);
    }

    @Test
    public void testRejectionForHavingAnInvalidToken() throws Exception {
        testFilter("invalid-token", true, true);
    }

    @Test
    public void testRejectWhenTokenConsistsOfOnlyWhiteSpaces() throws Exception {
        testFilter("   ", false, true);
    }

    @Test
    public void testApproval() throws Exception {
        testFilter("valid-token", false, false);
    }

    private void testFilter(String authorizationToken, Boolean rejectToken, Boolean unauthorized) throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        if (authorizationToken != null) {
            request.header("Authorization", authorizationToken);
        }

        Captcha captcha = token -> !rejectToken;

        MockMvcBuilders
                .standaloneSetup(new CaptchaFilterMockController())
                .addFilters(new CaptchaFilter(captcha))
                .build()
                .perform(request)
                .andExpect(unauthorized ? status().isUnauthorized() : status().isOk());
    }
}
