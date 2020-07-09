package com.elfari.desafio.swagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.elfari.desafio.security.SecurityConstants;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Value("${swagger.path}")
	private String swaggerPath;

	@Bean
	public Docket allApi() {
		
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		parameterBuilder.name(SecurityConstants.HEADER_STRING).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(parameterBuilder.build());
		
		Set<String> protocols = new HashSet<String>();
		protocols.add("http");
		protocols.add("https");
		
		return new Docket(DocumentationType.SWAGGER_2)
				.host(swaggerPath)
				.groupName("All")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.any())
				.build()
				.protocols(protocols)
				.ignoredParameterTypes(ApiIgnore.class)
				.enableUrlTemplating(true)
				.globalOperationParameters(parameters);
	}
	
	private ApiInfo apiInfo() {

        @SuppressWarnings("rawtypes")
		ApiInfo apiInfo = new ApiInfo(
                "Usuarios API REST",
                "API REST de gestão de usuários.",
                "1.0",
                "Terms of Service",
                new Contact("Maicon Espindula", "",
                        "mespindula@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }
}
