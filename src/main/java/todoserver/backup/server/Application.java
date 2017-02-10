
package todoserver.backup.server ;


import org.springframework.boot.SpringApplication ;
import org.springframework.boot.autoconfigure.SpringBootApplication ;
import org.springframework.context.annotation.Bean ;
import org.springframework.http.ResponseEntity ;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory ;
import org.springframework.retry.annotation.EnableRetry ;
import org.springframework.scheduling.annotation.EnableScheduling ;
import org.springframework.web.client.RestTemplate ;
import org.springframework.web.servlet.config.annotation.EnableWebMvc ;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry ;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter ;

import com.fasterxml.jackson.databind.ObjectMapper ;

import springfox.documentation.builders.PathSelectors ;
import springfox.documentation.builders.RequestHandlerSelectors ;
import springfox.documentation.spi.DocumentationType ;
import springfox.documentation.spring.web.plugins.Docket ;
import springfox.documentation.swagger2.annotations.EnableSwagger2 ;
import todoserver.backup.server.util.JsonUtil ;

@EnableRetry
@EnableWebMvc
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter
{

    public static void main ( String[] args )
    {
        SpringApplication.run ( Application.class, args ) ;
    }

    @Override
    public void addResourceHandlers ( ResourceHandlerRegistry registry )
    {
        registry.addResourceHandler ( "swagger-ui.html" ).addResourceLocations ( "classpath:/META-INF/resources/" ) ;
        registry.addResourceHandler ( "/webjars/**" ).addResourceLocations ( "classpath:/META-INF/resources/webjars/" ) ;
        super.addResourceHandlers ( registry ) ;
    }

    @Bean
    public Docket api ()
    {
        return new Docket ( DocumentationType.SWAGGER_2 ).select ()
                .apis ( RequestHandlerSelectors.basePackage ( "todoserver.backup.server.web.rest" ) ).paths ( PathSelectors.any () )
                .build ().genericModelSubstitutes ( ResponseEntity.class ) ;
    }

    @Bean
    public ObjectMapper createMapper ()
    {
        return JsonUtil.getMapper () ;
    }

    @Bean
    public RestTemplate createRestTemplate ()
    {
        return new RestTemplate ( new HttpComponentsClientHttpRequestFactory () ) ;
    }

}
