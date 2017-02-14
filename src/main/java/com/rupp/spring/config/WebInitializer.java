package com.rupp.spring.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//equal web.xml

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
 
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { MvcConfig.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { MvcConfig.class };
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }
    
//    @Override
//    protected Filter[] getServletFilters() {
//    	Filter [] singleton = { new CORSFilter()};
//    	return singleton;
//    }
 
}