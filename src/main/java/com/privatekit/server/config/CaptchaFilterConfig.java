package com.privatekit.server.config;

import com.privatekit.server.filter.CaptchaFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaFilterConfig {

    private final CaptchaFilter filter;

    public CaptchaFilterConfig(CaptchaFilter filter) {
        this.filter = filter;
    }

    @Bean
    public FilterRegistrationBean<CaptchaFilter> captchaFilterRegistration() {
        FilterRegistrationBean<CaptchaFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(
                "/v1.0/*/survey",
                "/v1.0/*/survey/*/response"
        );
        return registrationBean;
    }
}
