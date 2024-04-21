@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/jsp/", ".jsp").cache(true);
	}
	
    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index"); // Map root URL to "index" view		
	}

    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
			.addResourceLocations("/static/");
	}

    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggingInterceptor())
			.addPathPatterns("/auth/**");
	}

    @Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON); // Set default content type
		configurer.favorPathExtension(true); // Enable content negotiation based on file extensions
		configurer.mediaType("xml", MediaType.APPLICATION_XML); // Define media type for XML
		configurer.mediaType("json", MediaType.APPLICATION_JSON); // Define media type for JSON
	}
    
}