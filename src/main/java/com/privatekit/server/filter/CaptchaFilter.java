package com.privatekit.server.filter;

import com.privatekit.server.services.captcha.Captcha;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Order(1)
public class CaptchaFilter extends OncePerRequestFilter {

    private final Captcha captcha;

    public CaptchaFilter(Captcha captcha) {
        this.captcha = captcha;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String token = request.getHeader("Authorization");

        if (token == null || token.trim().isEmpty()) {
            unauthorized(response);
            return;
        }

        final Boolean valid = captcha.verify(token);

        if (valid) {
            chain.doFilter(request, response);
        } else {
            unauthorized(response);
        }
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        response.sendError(UNAUTHORIZED.value(), "Unauthorized");
    }
}
