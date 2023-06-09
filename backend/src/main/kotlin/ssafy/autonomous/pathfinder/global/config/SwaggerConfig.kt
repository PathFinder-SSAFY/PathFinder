package ssafy.autonomous.pathfinder.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2




@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ssafy.autonomous.pathfinder"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .pathMapping("/")

    }

    /*
    * Swagger 설정
    * */
    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
            .title("PathFinder")
            .description("PathFinder Swagger")
            .version("3.0")
            .build()
    }
}

