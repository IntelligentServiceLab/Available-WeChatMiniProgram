package com.sucker.servicebase;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springDoc-swagger标准配置
 *
 * @author huang cheng 2021/8/13
 */
@Configuration
public class SpringDocSwaggerConfig {

  private static final String basePackage = "com.sucker"; // 需要扫描api路径
  private static final String headerName = "Authorization"; // 请求头名称

  @Bean
  public GroupedOpenApi usersGroup() {
    return GroupedOpenApi.builder()
        .group("users")
        .addOperationCustomizer(
            (operation, handlerMethod) -> {
              operation.addSecurityItem(new SecurityRequirement().addList(headerName));
              return operation;
            })
        .packagesToScan(basePackage)
        .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    Components components = new Components();
    // 添加右上角的统一安全认证
    //        components.addSecuritySchemes(headerName,
    //                new SecurityScheme()
    //                        .type(SecurityScheme.Type.APIKEY)
    //                        .scheme("basic")
    //                        .name(headerName)
    //                        .in(SecurityScheme.In.HEADER)
    //                        .description("请求头")
    //        );

    return new OpenAPI().components(components).info(apiInfo());
  }

  private Info apiInfo() {
    Contact contact = new Contact();
    contact.setEmail("1903629378@qq.com");
    contact.setName("sucker");
    contact.setUrl("");
    return new Info()
        .title("state-swagger文档")
        .version("1.0")
        .contact(contact)
        .description("")
        .license(new License().name("Apache 2.0").url("http://springdoc.org"));
  }
}
