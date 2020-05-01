package com.privatekit.server.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testFilterChainDoChainIfCaptchaIsValidWithSimpleToken() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter();
        HttpServletRequest request = mockRequestWithAuthorizationHeader("Basic simpleToken");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testFilterChainDoChainIfCaptchaIsValidWithHCaptchaToken() throws ServletException, IOException {
        CaptchaFilter filter = makeFilter();
        HttpServletRequest request = mockRequestWithAuthorizationHeader("Basic P0_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNza2V5Ijoic2lDaytDVXVQekVLa2dUTGpkUldhbnZCQTV3Y2FpaVBoYkpBc3ZoSWJ0NHpwUDF0VDFSZXMrNlFwMGdrS1dpOVp2cU0wTkhhby9NZ0pDZmcrUGVqZ2lWZDhZS1lBMDhlaGhwM084ZFdHL1pEQWsrb3dzYVdLZFdDUVZHUVJDNlcwbzV5VFFnNVZDdWlub2d2V20vbDNMcldRNFNlWHZLeHM1NGZoZURqSlg5NVRjbjlTdVh0ekZaWlE0M2FIejNDSmpQU1l0anZiMUNOUVpuQUdrWEUwSkVwdlE4YUppMnR1VFloUlgrQ3c4cCtEWGVWY1Y3bkNFL2ZjWjN2bWdhbVFVc2ZZNmducHVlTE1QSTZUM0dkZXZITTJaNnJnMXRUakhlQUNiRkdSSUR3NXNYYTdnTm1RTG5pR21uRW1GaGFkemt5VXZXVDBKakdhL0xZd3NQb2RwSUE4RnhRUmVQc1VpWVFMVlAva3MwQW43aXdVb1c2cUFDUzQvTWlhVlJwN1hGWTZhSnZNQXBkWm1acCt0WE53QXhuTlVLa2lROEt3VnNIdHkrNncva2FERnl6VUMySEZlQ202SjJ1dzZUTWw1MHVhSjVzWDZpa0ZjNzVobkpjZ0tPMFBTQ21QbzMxTUdSRUlncGpnZDRaODl4eXB0cEtSdkM1TmZZZ2x5VVNxUXJmb2VBSlNTdkl3Sk5oamprOFM2QktEUnJoS2o1Tnl3YklsWW1LQWxSUHY1bm5SOVhRN2RST2RJVlg3Sjl6YUVwc1djRFZqaUZCNTgxQ3J1Y2pON254L3l5Wmd3UE5obXlSbEd4YmkrVDB4N2hxendDaTBEMUxqNGlZdlBoSUlOZ0hJN0I3Y0dPR0dQYTVvSkRPODlibHpjZExVVm5HenBpajBPL0lEcElSM20vdjRMV1NxcTAyeHF4OWFTblc2dlV0YjhiVUJTckFXVy9nbVhETTdTNmJGNEZRVnkvcXhNblNCVkp4bXRsU2VDWnhiMXNPUWY0M01TbEdTc2dTcURZaCtmRDZnUXRlRGdXOXJEa0JxOVFxUGsxZWtwL01QNlVZTDMvVFJSQWh5UERveE05ZndGbTNvWGw4WTY4Y2RxQmQ3bno0d3lyZHQ3WXBmYXMycHJlSHpHeEQrM2dBaG43TGFnWmFYNEtVYnR2ckh2TEpiT1VJd1JzZVB5dEhqeGpNalRCM0Rnc3BwRjN0NXphZi83YmVEbmd5UHNIMkhkQkdhQ3Y2MVlOT1NqYkJRWExHM3VUTTRpNi9Pdm9iemY4MTE3RDNzblNQWTJqMWw4MjNjTWlLMDQvV09xd1N1WkJNV0l6bjNNM210K3FPMUdVckxFRjhJNFNEODBSd0xEd3p1dXZnUlhjc1d3QVNPc0JPeTBZZ1pQSWZsTG4yYTBPdStxOEQyNDRMK1IrWkQ0eXdtdDhoYXFpeHBEYnB0TFZWM1E2azNYUXB6bUVtZzhsZHJKV2tQclNLUEdjcjNkeUQyVTF6QlFlZ1BIVVBhSTZTL2hEWjBScVREb3hXUFozcnVLMUhscmdCS21oOVU0YjRoelpGY0FDSHlkQnVTRE11bjVJcUJGcHkrYUVmbktKZnI0RHlNSlVFRk1xbFNvRXViN2JybytVTDIwUktoaW5hMFExUFZiYXF1LyswRitIbFk3bHU3eGp3TFRTY3Y1bXBsMVdwTWJ2bG9kNnVlL2pYMzVYZ0ZDQ2FhNGZhczRjaDc5Y1VUYjlPb3dHdTByYkhYS2dac1IrTmRPTG9KWjNBMVR0dEJad0ZtMlNUdVhXb3F1M1RHblM0ZVB3TzBHWU16YU0rQVZsYkdoNVlGUnluY0sxNVhERGZKUDhVWFp4MXgxREtaam1nWXN3Mk9Qb3E5V2szUkJjY2Erb044TEszS1VaUjR0SitWelNXRjN5U3Q3cWplTVdEMjFwbzR1cmpsREtKYk9SL3ExaW43VDRsUXN0TURPTVFIVGJvOFltdENXSThtVXl2N0Flck91SXpWangxS1ByeE12V0hoeFdHL25nTFBoZSt4emNCWUcwMERCQXdhakZsSGNmcU1jdmtYSS9ZZk9tWmJ5dzlRY3lLbFdHbmJCSkpjWWszR0hYN2licVVmWldpRG00SlU3NWo5UkhFT2YvUG1jK3l6eFhONVlmN0p0YUVMdmJPZ0JyYVRSYkcvcWprS081TVRKczV4SHgyeHNIZ3VLRVhybnAvanRtcE1JdXF1T1ZKU1lWMnp1cExHZUt0YlpJRE53QWZNLzg2dHNsMnRqRUVvSGc4Y2k5NmtVbXdlT0g2M2IvT01lZnhNSzQzUVZiQnc3Yk5yVGFRamJwT3h1aUIvWXdibFpzWXNOK0xCdTJNVDNoM0F0eHMxTjhhajB3TG9Tdk1NU2U2cy8xSG1RT3FJWGgwNHl2c0JrdndSeHBtYkJuQWNyZkhBazBrZG93SHcrdDZ3RXBoMU9UQ2huT2xGVmgwT0oxMGsvOFRSWmt4YVFaeVNqRjRKMklyZUpsWlV1UkZ3bExiZjhIa1NFMGhiS2pDS0pQQks2VlhuT0xqVlJ5UmpUN3BSWVJqcTQweXA1bzFFV0FGdUdhTENtV0J0aEVSTkVJMkFIenh3MEw5eThwQW1EM0NrRGsrTXlGV0pGVmxOVzUxWG1meXJxRURwY2NOb2trUnZpb1hha2pZbjhLcmdqdTZPZ289RW5Id2lsK3ZkWXlhNytlMyIsImV4cCI6MTU4ODE5OTEzOCwic2hhcmRfaWQiOjcyMTc0ODMzMSwicGQiOjAsImVrZXkiOiJmMDNiZTIwMS01YjgwLTQwMjItYjUxNi0zMDE5ZmM4MzU4YTkifQ.UW0MObpDS8MWA7MfxmoopM5tXIk_Adze9YI3ojXPj-8");
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
