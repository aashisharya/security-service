package com.omniwyse.security;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import com.omniwyse.security.config.RedisConfig;

public class RedisInit extends AbstractHttpSessionApplicationInitializer { 

    public RedisInit() {
            super(RedisConfig.class); 
    }

}
