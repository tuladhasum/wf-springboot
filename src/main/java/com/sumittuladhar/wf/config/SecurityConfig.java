package com.sumittuladhar.wf.config;

import com.sumittuladhar.wf.security.CustomAccessDeniedHandler;
import com.sumittuladhar.wf.security.CustomAuthenticationEntryPoint;
import com.sumittuladhar.wf.security.KeycloakRoleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final KeycloakRoleMapper keycloakRoleMapper;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(SecurityProperties securityProperties,
                          KeycloakRoleMapper keycloakRoleMapper,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.securityProperties = securityProperties;
        this.keycloakRoleMapper = keycloakRoleMapper;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakRoleMapper);

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> {
                // Configure public paths from application.yml
                authorize.requestMatchers(securityProperties.getPublicPaths().toArray(String[]::new)).permitAll();

                // Configure role mappings from application.yml
                securityProperties.getRoleMappings().forEach((path, role) -> {
                    String formattedPath = path.startsWith("/") ? path : "/" + path;
                    authorize.requestMatchers(formattedPath).hasAuthority(role);
                });

                // Secure all other paths
                authorize.anyRequest().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }
}
