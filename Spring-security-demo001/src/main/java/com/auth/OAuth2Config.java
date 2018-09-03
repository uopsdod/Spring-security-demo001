package com.auth;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
AUTH_STEP01: A user, as the resource owner, initiates a request to a resource in the resource server via the OAuth client.
AUTH_STEP02: The OAuth client sends the resource owner a redirection to the authorization server.
AUTH_STEP03: The resource owner authenticates and optionally authorizes with the authorization server.
AUTH_STEP04: The authorization server presents a form to the resource owner to grant access.
AUTH_STEP05: The resource owner submits the form to allow or to deny access.
AUTH_STEP06: Based on the response from the resource owner, the following processing occurs:
	If the resource owner allows access, the authorization server sends the OAuth client a redirection with the authorization grant code or the access token.
	If the resource owner denies access, the request is redirected to the OAuth client but no grant is provided.
AUTH_STEP07: The OAuth client sends the following information to the token endpoint (authorization server).
	Authorization grant code
	Client ID
	Client secret or client certificate
AUTH_STEP08: If verified, the authorization server sends the OAuth client an access token and optionally a refresh token.
AUTH_STEP09: The OAuth client sends the access token to the resource server to request protected resources.
AUTH_STEP10: If the access token is valid for the requested resources, the OAuth client can access the protected resources.
 * @author stsai
 *
 */
public class OAuth2Config {  
      
    public static final String OAUTH_CLIENT_ID = "oauth_client";  
    public static final String OAUTH_CLIENT_SECRET = "oauth_client_secret";  
    public static final String RESOURCE_ID = "my_resource_id";  
    public static final String[] SCOPES = { "read", "write" };  
  
    @Configuration  
    @EnableAuthorizationServer  
    static class OAuthAuthorizationConfig extends AuthorizationServerConfigurerAdapter {  
        @Override  
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {  
        	// AUTH_STEP07: The OAuth client sends the following information to the token endpoint (authorization server).
        	// 		Authorization grant code
        	// 		Client ID
        	// 		Client secret or client certificate
            clients.inMemory()  
                    .withClient(OAUTH_CLIENT_ID)  
                    .secret(OAUTH_CLIENT_SECRET)  
                    .resourceIds(RESOURCE_ID)  
                    .scopes(SCOPES)  
                    .authorities("ROLE_USER")  
                    .authorizedGrantTypes("authorization_code", "refresh_token")  
                    .redirectUris("http://default-oauth-callback.com")  
                    .accessTokenValiditySeconds(60*30) // 30min  
                    .refreshTokenValiditySeconds(60*60*24); // 24h  
            // AUTH_STEP08: If verified, the authorization server sends the OAuth client an access token and optionally a refresh token.
        }

		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			security.allowFormAuthenticationForClients();
		}  
        
        
        
        
    }  
      
    @Configuration  
    @EnableResourceServer  
    static class OAuthResourceConfig extends ResourceServerConfigurerAdapter {  
        @Override  
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {  
            resources.resourceId(RESOURCE_ID);  
        }  
        @Override  
        public void configure(HttpSecurity http) throws Exception {  
            http.authorizeRequests()  
            	// AUTH_STEP01: A user, as the resource owner, initiates a request to a resource in the resource server via the OAuth client.
            	// AUTH_STEP09: The OAuth client sends the access token to the resource server to request protected resources.
            	// AUTH_STEP10: If the access token is valid for the requested resources, the OAuth client can access the protected resources.
                .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")  
                .antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')")
                .and()
                // AUTH_STEP02: The OAuth client sends the resource owner a redirection to the authorization server.
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                ;  
        }  
        
        @Bean
        public AuthenticationEntryPoint unauthorizedEntryPoint() {
            return (request, response, authException) -> {
            	response.sendRedirect("/authorize");
            };
        }
    }  
      
    /**
     * This is where resource owners authorize OAuthClient to do following things
     *
     * About @Order
     * With SpringBoot must set @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
     * on WebSecurityConfiguration or Spring Security will
     * put UsernamePasswordAuthenticationFilter before OAuth2AuthenticationProcessingFilter.
     * It means OAuth2 authentication of resource server will not work.
     *
     */
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    @Configuration  
    @EnableWebSecurity  
    static class SecurityConfig extends WebSecurityConfigurerAdapter {  
        @Override  
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {  
        	// AUTH_STEP03: The resource owner authenticates and optionally authorizes with the authorization server.
            auth.inMemoryAuthentication()  
                .withUser("user").password("123").roles("USER")  
                .and()  
                .withUser("admin").password("123").roles("ADMIN");  
        }  
  
        @Override  
        protected void configure(HttpSecurity http) throws Exception {  
            http.csrf().disable();  
            http.authorizeRequests()  
            	// AUTH_STEP03: The resource owner authenticates and optionally authorizes with the authorization server.
                .antMatchers("/oauth/authorize").authenticated()  // when a resource owner wants to allow access and return authorization grant code back, he/she needs to be one of the users in the database 
                // AUTH_STEP04: The authorization server presents a form to the resource owner to grant access.
                // AUTH_STEP05: The resource owner submits the form to allow or to deny access.
                // AUTH_STEP06: Based on the response from the resource owner, the following processing occurs:
            	// 		If the resource owner allows access, the authorization server sends the OAuth client a redirection with the authorization grant code (or the access token).
            	// 		If the resource owner denies access, the request is redirected to the OAuth client but no grant is provided.
                .and()  
                .httpBasic().realmName("OAuth Server");  
        }  
    }  
  
}  
