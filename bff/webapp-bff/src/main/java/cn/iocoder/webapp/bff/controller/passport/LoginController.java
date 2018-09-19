package cn.iocoder.webapp.bff.controller.passport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/passport")
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public String login(@RequestParam("mobile") String mobile,
                        @RequestParam("password") String password) {
        OAuth2AccessToken token = postAccessToken(mobile, password);
        return "success";
    }

    private OAuth2AccessToken postAccessToken(String mobile, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("username", mobile);
        formData.add("password", password);
        formData.add("grant_type", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader("fooClientIdPassword", "secret"));

        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        return restTemplate.exchange("http://user-service/oauth/token", HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, String>>(formData, headers), OAuth2AccessToken.class).getBody();
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {
        String creds = String.format("%s:%s", clientId, clientSecret);
        try {
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

}