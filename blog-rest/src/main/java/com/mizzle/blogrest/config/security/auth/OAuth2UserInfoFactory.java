package com.mizzle.blogrest.config.security.auth;

import java.util.Map;

import com.mizzle.blogrest.config.security.auth.company.Facebook;
import com.mizzle.blogrest.config.security.auth.company.Github;
import com.mizzle.blogrest.config.security.auth.company.Google;
import com.mizzle.blogrest.config.security.auth.company.Kakao;
import com.mizzle.blogrest.config.security.auth.company.Naver;
import com.mizzle.blogrest.config.security.error.CustomAuthenticationException;
import com.mizzle.blogrest.entity.user.Provider;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(Provider.google.toString())) {
            return new Google(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.facebook.toString())) {
            return new Facebook(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.github.toString())) {
            return new Github(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.naver.toString())) {
            return new Naver(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.kakao.toString())) {
            return new Kakao(attributes);
        } else {
            throw new CustomAuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
