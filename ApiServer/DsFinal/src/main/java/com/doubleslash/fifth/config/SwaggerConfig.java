package com.doubleslash.fifth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).consumes(getConsumeContentTypes())
            .produces(getProduceContentTypes())
            .apiInfo(getApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.doubleslash.fifth.controller")) //Controller Path
            .paths(PathSelectors.ant("/**")) //URL Path
            .build()
            .securityContexts(securityContext())
            .securitySchemes(Arrays.asList(apiKey()));
    }
 
    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }
 
    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }
 
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("DsFinal")
            .description("API Docs")
            .version("1.0")
            .build();
    }
 
    /**
     * 404 Not Found가 발생하는 경우 swagger-ui.html 위치를 추가
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    private ApiKey apiKey() {
    	return new ApiKey("idToken", "Authorization", "header"); 
    }
    
    private List<SecurityContext> securityContext() { 
    	List<SecurityContext> securityContexts = new ArrayList<>();
    	String paths[] = {
                   "/test"
        };
    	
        for (String path: paths) {
        	securityContexts.add(SecurityContext.builder()
        			.securityReferences(defaultAuth())
        			.forPaths(PathSelectors.ant(path))
        			.build());
        }
        
        return securityContexts;
    }


	List<SecurityReference> defaultAuth() { 
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
        authorizationScopes[0] = authorizationScope; 
        return Arrays.asList(new SecurityReference("idToken", authorizationScopes)); 
    }
	
}