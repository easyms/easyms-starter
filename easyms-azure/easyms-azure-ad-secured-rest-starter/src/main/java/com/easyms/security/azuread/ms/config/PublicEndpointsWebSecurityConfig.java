package com.easyms.security.azuread.ms.config;

import com.easyms.security.azuread.ms.filter.CORSFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

/**
 * @author khames.
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@AllArgsConstructor
public class PublicEndpointsWebSecurityConfig {

    private final CORSFilter corsFilter;
    private final RoutesHandler routesHandler;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> {
            web.ignoring()
                    .requestMatchers(routesHandler.technicalEndPoints())
                    .requestMatchers(routesHandler.publicEndpoints());
        };
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class);
        return http.build();
        // @formatter:on
    }
}
