package com.privatekit.services.filter;

import com.privatekit.services.services.captcha.Captcha;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class CaptchaFilter extends OncePerRequestFilter {

    private final Captcha captcha;

    public CaptchaFilter(Captcha captcha) {
        this.captcha = captcha;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token == null || token.trim().isEmpty()) {
            unauthorized(response);
            return;
        }

        Boolean valid = captcha.verify(token);

        if (valid) {
            chain.doFilter(request, response);
        } else {
            unauthorized(response);
        }
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        response.sendError(401, "Unauthorized");
    }
}
