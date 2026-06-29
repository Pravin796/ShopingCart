package com.pravin.shopping_cart.auth;

import com.pravin.shopping_cart.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registery) {
        registery.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                 .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll();
    }
}
