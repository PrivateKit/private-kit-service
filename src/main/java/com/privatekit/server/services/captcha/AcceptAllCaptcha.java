package com.privatekit.server.services.captcha;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class AcceptAllCaptcha implements Captcha {

    @Override
    public Boolean verify(String token) {
        return true;
    }
}
