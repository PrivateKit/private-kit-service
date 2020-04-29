package com.privatekit.server.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class CaptchaFilterTest {

    @Test
    public void testShouldNotFilterFalse() {
        CaptchaFilter filter = makeFilter();
        assertFalse(filter.shouldNotFilter(mockRequestWithPathAndMethod("/path", "POST")));
    }

    @Test
    public void testShouldNotFilterTrue() {
        CaptchaFilter filter = makeFilter();
        assertTrue(filter.shouldNotFilter(mockRequestWithPathAndMethod("/invalid", "POST")));
        assertTrue(filter.shouldNotFilter(mockRequestWithPathAndMethod("/path", "invalid")));
    }

    @Test
    public void testResponseSendErrorIsCalledWhenAuthorizationHeaderIsNull() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter();
        HttpServletRequest request = mockRequestWithAuthorizationHeader(null);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(response).sendError(UNAUTHORIZED.value(), "Unauthorized");
    }

    @Test
    public void testResponseSendErrorIsCalledWhenAuthorizationHeaderDoesNotMatchPattern() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter();
        HttpServletRequest request = mockRequestWithAuthorizationHeader("Patterns are for losers!");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(response).sendError(UNAUTHORIZED.value(), "Unauthorized");
    }

    @Test
    public void testResponseSendErrorIfCaptchaIsInvalid() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter(true);
        HttpServletRequest request = mockRequestWithAuthorizationHeader("Basic someToken");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(response).sendError(UNAUTHORIZED.value(), "Unauthorized");
    }

    @Test
    public void testFilterChainDoChainIfCaptchaIsValid() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter();
        HttpServletRequest request = mockRequestWithAuthorizationHeader("Basic someToken");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    private CaptchaFilter makeFilter() {
        return makeFilter(false);
    }

    private CaptchaFilter makeFilter(Boolean rejectToken) {
        CaptchaFilterPathMatcherElement matcherElement = CaptchaFilterPathMatcherElement.from("/path", "POST", "GET");
        CaptchaFilterPathMatcher matcher = CaptchaFilterPathMatcher.with(matcherElement);
        return new CaptchaFilter(token -> !rejectToken, matcher);
    }

    private HttpServletRequest mockRequestWithPathAndMethod(String path, String method) {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn(path);
        Mockito.when(request.getMethod()).thenReturn(method);
        return request;
    }

    private HttpServletRequest mockRequestWithAuthorizationHeader(String header) {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(header);
        return request;
    }
}
