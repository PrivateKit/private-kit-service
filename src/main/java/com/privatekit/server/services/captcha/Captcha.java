package com.privatekit.server.services.captcha;

public interface Captcha {
    Boolean verify(String token);
}
