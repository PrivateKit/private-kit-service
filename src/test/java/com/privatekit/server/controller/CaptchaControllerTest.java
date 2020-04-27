package com.privatekit.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class CaptchaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testResponse() throws Exception {
        mvc
                .perform(get("/v1.0/captcha"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("captcha"))
                .andExpect(content().string("<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                        "    <script src=\"https://hcaptcha.com/1/api.js\" async defer></script>\n" +
                        "    <script type=\"text/javascript\">\n" +
                        "        var onloadCallback = function () {\n" +
                        "        };\n" +
                        "        var onDataCallback = function (response) {\n" +
                        "            window.ReactNativeWebView.postMessage(response);\n" +
                        "        };\n" +
                        "        var onDataExpiredCallback = function (error) {\n" +
                        "            console.log(error);\n" +
                        "            window.ReactNativeWebView.postMessage(\"expired\");\n" +
                        "        };\n" +
                        "        var onDataErrorCallback = function (error) {\n" +
                        "            console.log(error);\n" +
                        "            window.ReactNativeWebView.postMessage(\"error\");\n" +
                        "        }\n" +
                        "    </script>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<div id=\"captcha\">\n" +
                        "    <div style=\"text-align: center; padding-top: 100px;\">\n" +
                        "        <div class=\"h-captcha\" style=\"display: inline-block; height: auto;\"\n" +
                        "             data-callback=\"onDataCallback\"\n" +
                        "             data-expired-callback=\"onDataExpiredCallback\"\n" +
                        "             data-error-callback=\"onDataErrorCallback\" data-sitekey=\"YOUR-SITE-KEY\">\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>"));
    }
}
