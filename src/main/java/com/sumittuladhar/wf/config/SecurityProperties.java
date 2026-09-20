package com.sumittuladhar.wf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private List<String> publicPaths = List.of(
        "/",
        "/api/hello",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    );

    private String rolePrefix = "ROLE_";
    private String claimName = "realm_access";
    private String rolesClaimName = "roles";
    private Map<String, String> roleMappings = new LinkedHashMap<>();

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }

    public String getRolePrefix() {
        return rolePrefix;
    }

    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    public String getRolesClaimName() {
        return rolesClaimName;
    }

    public void setRolesClaimName(String rolesClaimName) {
        this.rolesClaimName = rolesClaimName;
    }

    public Map<String, String> getRoleMappings() {
        return roleMappings;
    }

    public void setRoleMappings(Map<String, String> roleMappings) {
        this.roleMappings = roleMappings;
    }
}
