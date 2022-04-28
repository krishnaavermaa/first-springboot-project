package com.in28minutes.springboot.security;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{


		@Override
		  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			
//	********* the below commented code doesnt work now as password encoding is required********
//		  	auth.inMemoryAuthentication().withUser("user1").password("secret1")
//		  	.roles("USER").and().withUser("admin1").password("secret2")
//		  	.roles("USER", "ADMIN");
			  
			  auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
			  .withUser("user1").password("secret1").roles("USER")
			  .and().withUser("admin1").password("secret2").roles("USER", "ADMIN");

			  
		  }
		@Override
		protected void configure(HttpSecurity http) throws Exception {
//			this.logger.debug("Using default configure(HttpSecurity). "
//					+ "If subclassed this will potentially override subclass configure(HttpSecurity).");
			
			
//			
//			http.authorizeRequests((requests) -> requests.anyRequest().authenticated());
//			http.formLogin();
			http.httpBasic().and().authorizeRequests()
				.antMatchers("/surveys/**").hasRole("USER")
				.antMatchers("/users/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN");
//				.and().csrf().disable()
//				.headers().frameOptions().disable();
//			
				
		}
	
}
