package com.mizzle.blogrest.config.security.auth.company;

import java.util.Map;

import com.mizzle.blogrest.config.security.auth.OAuth2UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Google extends OAuth2UserInfo{

    public Google(Map<String, Object> attributes) {
        super(attributes);
        //TODO Auto-generated constructor stub
        log.info("GoogleOAuth2UserInfo={}",attributes);
    }

    @Override
    public String getId() {
        
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        
        return (String) attributes.get("picture");
    }
    
}