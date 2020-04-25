package com.privatekit.services.services.captcha;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("prod")
public class HCaptcha implements Captcha {

    private final String secret;
    private final String verifyUrl;

    public HCaptcha(
            @Value("${captcha.secret}") String secret,
            @Value("${captcha.verify-url}") String verifyUrl
    ) {
        this.secret = secret;
        this.verifyUrl = verifyUrl;
    }

    @Override
    public Boolean verify(String token) {
        try {
            ResponseEntity<String> response = new RestTemplate()
                    .postForEntity(
                            URI.create(verifyUrl),
                            makeHttpEntity(makeHeaders(), token),
                            String.class);
            return verifyFromResponse(response);
        } catch (Exception e) {
            return false;
        }
    }

    private HttpHeaders makeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<MediaType> acceptMediaTypes = new ArrayList<>();
        acceptMediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptMediaTypes);
        return headers;
    }

    private HttpEntity<MultiValueMap<String, String>> makeHttpEntity(HttpHeaders headers, String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("response", token);
        map.add("secret", secret);
        return new HttpEntity<>(map, headers);
    }

    private Boolean verifyFromResponse(ResponseEntity<String> response) {
        String hCaptchaResponse = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(hCaptchaResponse).path("success").booleanValue();
        } catch (Exception e) {
            return false;
        }
    }
}
