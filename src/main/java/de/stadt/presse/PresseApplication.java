package de.stadt.presse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class PresseApplication {

//@Bean
//public  WebMvcConfigurer configurer(){
//  return new WebMvcConfigurer() {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//      String myExternalFilePath = "file:///D:/";
//      registry.addResourceHandler("/**").addResourceLocations(myExternalFilePath);
//    }
//  };
//}


	public static void main(String[] args) {

	  SpringApplication.run(PresseApplication.class, args);


	}
}
