package org.ckxnhat.identity.config;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.identity.config.security.CustomUserDetails;
import org.ckxnhat.identity.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-24 19:09:27.825
 */

@Configuration
@RequiredArgsConstructor
public class AccessTokenCustomizerConfig {
    private final UserRepository userRepository;
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                CustomUserDetails user = (CustomUserDetails) context.getPrincipal().getPrincipal();
                context.getClaims().claims(claim -> {
                    claim.put("role",context
                            .getPrincipal()
                            .getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
                    );
                    claim.put("uid", user.getUserId());
                });
            }
        };
    }
}
