package com.sumittuladhar.wf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Automatically redirect /swagger-ui (no trailing slash) to /swagger-ui/index.html
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
        
        // Optional: Redirect root context "/" to Swagger UI for convenience
        registry.addRedirectViewController("/", "/swagger-ui/index.html");
    }
}
