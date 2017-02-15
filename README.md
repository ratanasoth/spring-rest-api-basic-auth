# Spring REST-API with BASIC spring security : it is used with maven3 build .

![alt tag](https://github.com/sophea/spring-rest-api-basic-auth/blob/master/basic-Authentication.png)

//start jetty server with maven plugin

$ mvn clean jetty:run


# Basic authentication Users :
```
bill/abc123 - ADMIN
tom/abc123  - USER
```

# Open browsers :

http://localhost:8080/api/countries/v1/all

# CRUD REST-APIs you must pass basic authentication header along with :
```
GET http://localhost:8080/api/categories/v1/all
GET http://localhost:8080/api/cagetoires/v1/{id}
POST http://localhost:8080/api/cagetoires/v1/{id}
DELETE http://localhost:8080/api/cagetoires/v1/{id}
PUT http://localhost:8080/api/cagetoires/v1/{id}
```

# curl commands :
```
curl 'http://localhost:8080/api/categories/v1/all' --user  'bill:abc123'  -H 'Connection: keep-alive' --compressed
curl 'http://localhost:8080/api/countries/v1/all'  --user 'tom:abc123'  -H 'Connection: keep-alive' --compressed
```

# Maven depedendcy

```
 <spring.version>4.3.5.RELEASE</spring.version>
    <springsecurity.version>4.1.1.RELEASE</springsecurity.version>
<!-- Spring Security -->
    <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
            <version>${springsecurity.version}</version>
    </dependency>
    <dependency>
          <groupId>org.springframework.security</groupId>
          <artifactId>spring-security-config</artifactId>
          <version>${springsecurity.version}</version>
    </dependency>
        
    <!-- Spring framework -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
  <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${spring.version}</version>
    </dependency>
```    


# WebInitializer.java //javacode  instead of web.xml

```
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
//      Filter [] singleton = { new CORSFilter()};
//      return singleton;
//    }
 
}


package com.rupp.spring.config;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@Import(value = { SecurityConfiguration.class })
@EnableWebMvc
@ComponentScan(value = {"com.rupp.spring.controller", "com.rupp.spring.service", "com.rupp.spring.dao"})
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        SkipNullObjectMapper skipNullMapper = new SkipNullObjectMapper();
        skipNullMapper.init();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(skipNullMapper);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        skipNullMapper.setDateFormat(formatter);
        
        converters.add(converter);
    }
}

==========================================================
SecurityWebApplicationInitializer.java
It is initial web application security and use websecurity @annotation
===========================================================
package com.rupp.spring.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Sophea <a href='mailto:smak@dminc.com'> sophea </a>
 * @version $id$ - $Revision$
 * @date 2017
 */

/**
The equivalent of Spring Security in web.xml file :

<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy
                </filter-class>
</filter>

<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
*/
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}
========================================================================
SecurityConfiguration.java : to setup Basic Security with REST-API 

package com.rupp.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

    public final static String REALM="MY_TEST_REALM";
    
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("tom").password("abc123").roles("USER");
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
        http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/**").hasAnyRole("ADMIN", "USER")
        .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());
    }
     
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
     
    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
===================================
package com.rupp.spring.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    
    @Override
    public void commence(final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException, ServletException {
        //Authentication failed, send error response.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
         
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 : " + authException.getMessage());
    }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(SecurityConfiguration.REALM);
        super.afterPropertiesSet();
    }
}
===========================================================

ref : http://websystique.com/spring-security/secure-spring-rest-api-using-basic-authentication/
```

# Use @Secure Annotation

http://docs.spring.io/autorepo/docs/spring-security/4.0.0.CI-SNAPSHOT/reference/htmlsingle/#jc

To use @Secure Annotation, first we should enable annotation-based security using the @EnableGlobalMethodSecurity annotation on any @Configuration instance. For example, the following would enable Spring Security’s @Secured annotation.
```
@Configuration
@Import(value = { SecurityConfiguration.class })
@EnableWebMvc
@ComponentScan(value = {"com.rupp.spring.controller", "com.rupp.spring.service", "com.rupp.spring.dao"})
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MvcConfig extends WebMvcConfigurerAdapter {
..
}
```

After then we can use @Secure annotation with controller REST-API:

```

@Controller
@RequestMapping("countries")
public class CountryController {
    @Autowired
    private CountryService service;
    
    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/v1/all", method = RequestMethod.GET)
    public ResponseEntity<Collection<String>> getAllCountries() {
     final Collection<String> countries = service.getAll();
     return new ResponseEntity<>(countries, HttpStatus.OK);

    }
}
```


