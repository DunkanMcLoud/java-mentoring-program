package ru.epam.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;


@Configuration
@ComponentScan(basePackages = "ru.epam.auth")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange()
                .pathMatchers("/error", "/oauth2/**", "login").permitAll()
                .pathMatchers("/swagger-ui/**").hasAuthority("dev")
                .anyExchange().authenticated()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .oauth2Login()
                .and()
                .oauth2Client()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return httpSecurity.build();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return new GrantedAuthoritiesMapper() {
            @Override
            public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
                return authorities.stream()
                        .filter(OAuth2UserAuthority.class::isInstance)
                        .map(grantedAuthority -> ((OAuth2UserAuthority) grantedAuthority).getAttributes().get("realm_access"))
                        .map(LinkedHashMap.class::cast)
                        .map(linkedHashMap -> linkedHashMap.get("roles"))
                        .map(ArrayList.class::cast)
                        .flatMap(Collection<String>::stream)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
            }
        };
    }
}
