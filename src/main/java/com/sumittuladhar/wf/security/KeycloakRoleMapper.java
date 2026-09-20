package com.sumittuladhar.wf.security;

import com.sumittuladhar.wf.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakRoleMapper implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(KeycloakRoleMapper.class);
    private final SecurityProperties securityProperties;

    public KeycloakRoleMapper(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        log.debug("Converting JWT token for subject: {}", jwt.getSubject());
        
        Map<String, Object> realmAccess = jwt.getClaim(securityProperties.getClaimName());
        if (realmAccess == null) {
            log.warn("No '{}' claim block found in JWT token", securityProperties.getClaimName());
            return Collections.emptyList();
        }

        if (!realmAccess.containsKey(securityProperties.getRolesClaimName())) {
            log.warn("Claim block '{}' does not contain roles nested key '{}'", 
                    securityProperties.getClaimName(), securityProperties.getRolesClaimName());
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get(securityProperties.getRolesClaimName());
        if (roles == null) {
            log.warn("Roles list is null under claim path {}.{}", 
                    securityProperties.getClaimName(), securityProperties.getRolesClaimName());
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = roles.stream()
                .map(roleName -> {
                    String authorityName = securityProperties.getRolePrefix() + roleName.toUpperCase();
                    log.debug("Mapping Keycloak role '{}' to authority '{}'", roleName, authorityName);
                    return new SimpleGrantedAuthority(authorityName);
                })
                .collect(Collectors.toList());

        log.info("Mapped authorities for user {}: {}", jwt.getSubject(), authorities);
        return authorities;
    }
}
