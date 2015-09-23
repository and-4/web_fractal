package com.example.webf.services;

import com.example.webf.domain.UserEntity;

/**
 * Provides processing service to set user authentication session
 * 
 * @author Arthur Vin
 */
public interface UserAuthenticationProviderService {

        /**
         * Process user authentication
         * 
         * @param user
         * @return
         */
        boolean processUserAuthentication(UserEntity user);
}
