package com.sumittuladhar.wf.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Secure Controller", description = "Endpoints requiring authentication and role verification")
@SecurityRequirement(name = "bearerAuth")
public class SecureController {

    @GetMapping("/api/secure/user")
    @Operation(summary = "Get user information", description = "Returns authentication details and roles extracted from JWT")
    public Map<String, Object> getUserInfo(JwtAuthenticationToken auth) {
        Map<String, Object> info = new HashMap<>();
        info.put("username", auth.getName());
        info.put("tokenAttributes", auth.getToken().getClaims());
        info.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return info;
    }

    @GetMapping("/api/secure/admin")
    @Operation(summary = "Get admin content", description = "Access restricted to users with ADMIN role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, String> getAdminContent() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access Granted. Welcome, Admin!");
        return response;
    }
}
