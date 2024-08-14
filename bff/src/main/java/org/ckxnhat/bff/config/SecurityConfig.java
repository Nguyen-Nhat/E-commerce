package org.ckxnhat.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-23 21:41:30.433
 */

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ServerOAuth2AuthorizationRequestResolver resolver) {
        return http
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/api/resource/**").permitAll()
                        .pathMatchers("/api/identity/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(auth -> auth.authorizationRequestResolver(resolver))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
    @Bean
    public ServerOAuth2AuthorizationRequestResolver pkceResolver(ReactiveClientRegistrationRepository repo) {
        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }
}
