package com.mizzle.blogrest.config.security.auth.company;

import java.util.HashMap;
import java.util.Map;

import com.mizzle.blogrest.config.security.auth.OAuth2UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Facebook extends OAuth2UserInfo{

    public Facebook(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
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
        
        if(attributes.containsKey("picture")) {
            
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
    
}
