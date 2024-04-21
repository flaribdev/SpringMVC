package com.teste.springmvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

	/* @Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/jsp/", ".jsp").cache(true);
	} */
	
    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index"); // Map root URL to "index" view		
	} 

    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
			.addResourceLocations("/static/");
	}

   /*  @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggingInterceptor())
			.addPathPatterns("/auth/**");
	} */

    @Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON); // Set default content type
		configurer.favorPathExtension(true); // Enable content negotiation based on file extensions
		configurer.mediaType("xml", MediaType.APPLICATION_XML); // Define media type for XML
		configurer.mediaType("json", MediaType.APPLICATION_JSON); // Define media type for JSON
	}

    /* @Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		// Clear the default converters
		converters.clear();
		// Use Jackson for XML
		converters.add(new MappingJackson2XmlHttpMessageConverter());
		// Use Gson for JSON
		converters.add(new GsonHttpMessageConverter());
	} */

    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/auth/**") 
			.allowedOrigins("http://localhost:8080") 
			.allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
			.allowedHeaders("*") // Allowed request headers
			.allowCredentials(false) 
			.maxAge(3600); 
	}


    /* @Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(new CustomExceptionResolver());
	}


    private static class CustomExceptionResolver implements HandlerExceptionResolver {

		@Override
		public ModelAndView resolveException(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

			if (ex instanceof Exception) {

				ModelAndView modelAndView = new ModelAndView();
				modelAndView.setViewName("some-exception");
				modelAndView.addObject("message", "SomeException occurred: " + ex.getMessage());
				return modelAndView;
			}
			return null;
		}
} */


//Thymeleaf configs
@Bean
public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");//If the encoding of the Java property file is UTF-8
    return messageSource;
  }
  /*
   * STEP 1 - Create SpringResourceTemplateResolver
  */
  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setPrefix("/WEB-INF/views/");
    templateResolver.setSuffix(".html");
    templateResolver.setCacheable(false);//Development Mode
    return templateResolver;
  }

  /*
   * STEP 2 - Create SpringTemplateEngine
  */
  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.setEnableSpringELCompiler(true);
    return templateEngine;
  }

  /*
   * STEP 3 - Create ThymeleafViewResolver
  */
  @Bean
  public ThymeleafViewResolver viewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setCharacterEncoding("UTF-8");
    viewResolver.setViewNames(new String[]{".html"});
    return viewResolver;
  }
}