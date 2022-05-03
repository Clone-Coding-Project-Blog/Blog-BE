package com.mizzle.blogrest.config.security.auth.company;

import java.util.Map;

import com.mizzle.blogrest.config.security.auth.OAuth2UserInfo;

public class Github extends OAuth2UserInfo{

    public Github(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        
        return ((Integer) attributes.get("id")).toString();
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
        
        return (String) attributes.get("avatar_url");
    }
    
}
