package com.privatekit.server.config;

import com.privatekit.server.filter.CaptchaFilterPathMatcher;
import com.privatekit.server.filter.CaptchaFilterPathMatcherElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean("captchaFilterPathMatcher")
    public CaptchaFilterPathMatcher getCaptchaFilterPathMatcher() {
        return CaptchaFilterPathMatcher.with(
                CaptchaFilterPathMatcherElement.from("/v1.0/*/survey", "GET", "POST"),
                CaptchaFilterPathMatcherElement.from("/v1.0/*/survey/*/response", "GET", "POST")
        );
    }
}
