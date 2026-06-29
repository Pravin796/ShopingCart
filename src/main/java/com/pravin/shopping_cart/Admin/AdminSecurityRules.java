package com.pravin.shopping_cart.Admin;

import com.pravin.shopping_cart.common.SecurityRules;
import com.pravin.shopping_cart.Users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registery) {
        registery.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());
    }
}
